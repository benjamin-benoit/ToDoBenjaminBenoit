package com.benben.todo.auth


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.benben.todo.R
import com.benben.todo.network.Api
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.emailLabel
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonRegister = signUpBtn
        buttonRegister.setOnClickListener {
            lifecycleScope.launch {
                if (emailLabel.text.isNotEmpty() && passwordLabel.text.isNotEmpty()) {
                    val myUser = LoginForm(
                        email = emailLabel.text.toString(),
                        password = passwordLabel.text.toString(),
                        passwordConfirmation = passwordConfSingUp.text.toString(),
                        firstName = firstNameLabel.text.toString(),
                        lastname = lastnameLabel.text.toString()
                        )
                    register(myUser)
                }
            }
        }
    }
    suspend fun register(user: LoginForm): LoginResponse? {
        val userResponse = Api.userService.signUp(user)
        return if (userResponse.isSuccessful) {
//            PreferenceManager.getDefaultSharedPreferences(context).edit {
//                putString(SHARED_PREF_TOKEN_KEY, fetchedToken)
//            }
            userResponse.body()
        } else {
            Toast.makeText(context, "text", Toast.LENGTH_LONG).show()
            return null
        }
    }
}
