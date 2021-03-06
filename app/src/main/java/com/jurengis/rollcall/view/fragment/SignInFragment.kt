package com.jurengis.rollcall.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jurengis.rollcall.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_signin.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_signin) {

    @Inject lateinit var auth : FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindEvents()
    }

    private fun bindEvents() {
        bindSignUpButton()
        bindLoginButton()
    }

    private fun bindLoginButton() {
        btnLogin.setOnClickListener {
            Log.d(TAG, "btnLogin:clicked")
            val email = tfSignInEmail.editText?.text.toString().trim()
            val password = tfSignInPassword.editText?.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val authenticationSuccessful = login(email, password)
                    updateUIAfterLogin(authenticationSuccessful)
                }
            }
        }
    }

    private fun bindSignUpButton() {
        tvSignUpButton.setOnClickListener {
            Log.d(TAG, "tvSignUpButton:clicked")
            findNavController().navigate(
                R.id.action_signInFragment_to_signUpFragment,
            )
        }
    }

    private suspend fun login(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            e.message?.let { Log.d(TAG, it) }
            false
        }
    }

    private suspend fun updateUIAfterLogin(authenticationSuccessful: Boolean) {
        withContext(Dispatchers.Main) {
            if (authenticationSuccessful && auth.currentUser != null) {
                Log.d(TAG, "signInWithEmailAndPassword:success")
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    R.id.action_signInFragment_to_courseListFragment
                )
            } else {
                Log.d(TAG, "signInWithEmailAndPassword:failure")
                Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val TAG = "SignInFragment"
    }
}