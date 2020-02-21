package com.benben.todo.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.benben.todo.R
import kotlinx.android.synthetic.main.fragment_authentication.*

class AuthenticationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_authentication, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonLogin = buttonLogin
        val buttonSignIn = buttonSignUp
        buttonLogin.setOnClickListener{
            findNavController().navigate(R.id.action_authenticationFragment_to_loginFragment)
        }
        buttonSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_authenticationFragment_to_signupFragment)
        }
    }
}
