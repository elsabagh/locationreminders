package com.udacity.project4.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.map
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.locationreminders.RemindersActivity
import kotlinx.android.synthetic.main.activity_authentication.*

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity()
{
    val SIGN_IN_REQUEST_CODE= 1
    private val viewModel: AuthenticationViewModel by viewModels<AuthenticationViewModel>()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        viewModel.authenticationState.observe(this, Observer { authenticationState ->
            Log.i("Abdelrahman", authenticationState.name)
            if (authenticationState == AuthenticationViewModel.AuthenticationState.AUTHENTICATED) {
                startActivity(Intent(this, RemindersActivity::class.java))
            }
        })

        login_btn.setOnClickListener {
            launchSignInFlow()
        }



    }
    fun checkUserAuthentication()
    {

    }
    private fun launchSignInFlow()
    {
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build())
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), SIGN_IN_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST_CODE)
        {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == RESULT_OK)
            {
                val intent=Intent(this, RemindersActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
                Log.i("Abdelrahman", "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
            }
            else
            {
                Log.i("Aabdelrahman", "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }
}
