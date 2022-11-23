package com.udacity.project4.authentication

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseUserLiveData : LiveData<FirebaseUser?>()
{
    val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        value = firebaseAuth.currentUser
    }

    val firebaseAuth=FirebaseAuth.getInstance()
    override fun onActive() {
        super.onActive()
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onInactive() {
        super.onInactive()
        firebaseAuth.removeAuthStateListener(authStateListener)
    }
}