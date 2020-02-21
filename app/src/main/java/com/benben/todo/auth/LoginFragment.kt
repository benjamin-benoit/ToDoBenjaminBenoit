package com.benben.todo.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.benben.todo.R
import com.benben.todo.network.Api
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonLogin = loginBtn
        buttonLogin.setOnClickListener {
            lifecycleScope.launch {
                if(emailLabel.text.isNotEmpty() && passwordLabel.text.isNotEmpty()){
                    val myUser = LoginForm(email = emailLabel.text.toString(), password = passwordLabel.text.toString())
                    login(myUser)
                }
            }
        }
    }

    suspend fun login(user: LoginForm): LoginResponse? {
        val userResponse = Api.userService.login(user)
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
