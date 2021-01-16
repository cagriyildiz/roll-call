package com.jurengis.rollcall.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.jurengis.rollcall.R
import kotlinx.android.synthetic.main.fragment_signin.*

class SignInFragment : Fragment(R.layout.fragment_signin) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindEvents()
    }

    private fun bindEvents() {
        bindSignUpButton()
    }

    private fun bindSignUpButton() {
        tvSignUpButton.setOnClickListener {
            Log.d(TAG, "bindSignUpButton:showSignUpFragment")
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.rollCallNavHostFragment, SignUpFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    companion object {
        private const val TAG = "SignInFragment"
    }
}