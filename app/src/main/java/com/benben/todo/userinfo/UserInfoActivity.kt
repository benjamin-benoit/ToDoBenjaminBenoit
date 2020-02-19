package com.benben.todo.userinfo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.benben.todo.R
import com.benben.todo.network.Api
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*

class UserInfoActivity: AppCompatActivity() {

    private val userWebService = Api.userService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        val takePictureButton = findViewById<Button>(R.id.take_picture_button)
        val uploadImageButton = findViewById<Button>(R.id.upload_image_button)

        takePictureButton.setOnClickListener{
            askCameraPermissionAndOpenCamera()
        }
        uploadImageButton.setOnClickListener{
            uploadImage()
        }
        val glide = Glide.with(this)
        lifecycleScope.launch {
            val userInfo = getInfo()
            glide.load(userInfo?.avatar).into(image_view)
        }
        button.setOnClickListener{
            val myUser = UserInfo(email = userEmail.text.toString(), firstName = userFirstName.text.toString(), lastName = userLastName.text.toString())
            intent.putExtra("userInfo", myUser as Serializable)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            handlePhotoTaken(data)
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Pour récupérer le bitmap dans onActivityResult
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data?.data)
            Glide.with(this).load(bitmap).into(image_view)
        }
    }

    private fun askCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // l'OS dit d'expliquer pourquoi on a besoin de cette permission:
                showDialogBeforeRequest()
            } else {
                // l'OS ne demande pas d'explication, on demande directement:
                requestCameraPermission()
            }
        } else {
            openCamera()
        }
    }

    private fun showDialogBeforeRequest() {
        // Affiche une popup (Dialog) d'explications:
        AlertDialog.Builder(this).apply {
            setMessage("On a besoin de la caméra ! 🥺")
            setPositiveButton(android.R.string.ok) { _, _ -> requestCameraPermission() }
            setCancelable(true)
            show()
        }
    }

    private fun requestCameraPermission() {
        // CAMERA_PERMISSION_CODE est défini par nous et sera récupéré dans onRequestPermissionsResult
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE )
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        }
    }

    private fun uploadImage() {
        // Pour ouvrir la gallerie:
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    private fun handlePhotoTaken(data: Intent?) {
        val image = data?.extras?.get("data") as? Bitmap
        Glide.with(this).load(image).into(image_view)

        val imageBody = imageToBody(image)
        lifecycleScope.launch {
            updateAvatar(imageBody)
        }
    }

    suspend fun getInfo(): UserInfo? {
        val tasksResponse = userWebService.getInfo()
        return if (tasksResponse.isSuccessful) {
            userFirstName.setText(tasksResponse.body()!!.firstName)
            userLastName.setText(tasksResponse.body()!!.lastName)
            userEmail.setText(tasksResponse.body()!!.email)
            tasksResponse.body()
        } else null
    }

    suspend fun updateAvatar(imageBody: MultipartBody.Part): UserInfo? {
        val tasksResponse = userWebService.updateAvatar(imageBody)
        return if (tasksResponse.isSuccessful) {
            tasksResponse.body()
        } else null
    }

    // Celle ci n'est pas très intéressante à lire
    // En gros, elle lit le fichier et le prépare pour l'envoi HTTP
    private fun imageToBody(image: Bitmap?): MultipartBody.Part {
        val f = File(cacheDir, "tmpfile.jpg")
        f.createNewFile()
        try {
            val fos = FileOutputStream(f)
            image?.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val body = RequestBody.create(MediaType.parse("image/png"), f)
        return MultipartBody.Part.createFormData("avatar", f.path, body)
    }

    companion object {
        const val CAMERA_PERMISSION_CODE = 42
        const val CAMERA_REQUEST_CODE = 2001
        const val GALLERY_REQUEST_CODE = 2002
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else if (requestCode == GALLERY_REQUEST_CODE && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            uploadImage()
        } else {
            Toast.makeText(this, "Si vous refusez, on peux pas prendre de photo ! 😢", Toast.LENGTH_LONG).show()
        }
    }
}