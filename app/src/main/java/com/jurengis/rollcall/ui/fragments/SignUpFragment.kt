package com.jurengis.rollcall.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.jurengis.rollcall.R
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpFragment : Fragment(R.layout.fragment_signup) {

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        bindEvents()
    }

    private fun bindEvents() {
        bindRegisterButton()
    }

    private fun bindRegisterButton() {
        btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val email = tfSignUpEmail.editText?.text.toString().trim()
        val password = tfSignUpPassword.editText?.text.toString().trim()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                auth.createUserWithEmailAndPassword(email, password)
                withContext(Dispatchers.Main) {
                    isUserLoggedIn()
                }
            }
        }
    }

    private fun isUserLoggedIn() {
        if (auth.currentUser == null) {
            Log.d(TAG, "isUserLoggedIn:failure")
            Toast.makeText(context, "Cannot create account", Toast.LENGTH_SHORT).show()
        } else {
            Log.d(TAG, "isUserLoggedIn:success")
            Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "SignUpFragment"
    }
}