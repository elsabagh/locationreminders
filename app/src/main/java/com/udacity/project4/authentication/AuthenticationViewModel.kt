package com.udacity.project4.authentication

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.firebase.ui.auth.data.model.User
import com.udacity.project4.locationreminders.RemindersActivity

class AuthenticationViewModel :ViewModel(){

    enum class AuthenticationState{AUTHENTICATED,UNAUTHENTICATED}

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        }
        else {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}