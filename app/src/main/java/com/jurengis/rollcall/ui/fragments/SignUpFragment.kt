package com.jurengis.rollcall.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.jurengis.rollcall.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_signup) {

    @Inject lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindEvents()
    }

    private fun bindEvents() {
        bindRegisterButton()
    }

    private fun bindRegisterButton() {
        btnRegister.setOnClickListener {
            val email = tfSignUpEmail.editText?.text.toString().trim()
            val password = tfSignUpPassword.editText?.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val signUpSuccessful = signUp(email, password)
                    updateUIAfterSignUp(signUpSuccessful)
                }
            }
        }
    }

    private suspend fun signUp(email: String, password: String): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            Log.d(TAG, "createUserWithEmailAndPassword:failure")
            e.message?.let { Log.d(TAG, it) }
            false
        }
    }

    private suspend fun updateUIAfterSignUp(signUpSuccessful: Boolean) {
        withContext(Dispatchers.Main) {
            if (signUpSuccessful && auth.currentUser != null) {
                Log.d(TAG, "createUserWithEmailAndPassword:success")
                Toast.makeText(context, "Account created successfully", Toast.LENGTH_SHORT).show()
            } else {
                Log.d(TAG, "createUserWithEmailAndPassword:failure")
                Toast.makeText(context, "Cannot create account", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val TAG = "SignUpFragment"
    }
}