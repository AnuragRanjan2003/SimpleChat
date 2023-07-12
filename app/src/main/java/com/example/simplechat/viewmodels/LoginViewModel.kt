package com.example.simplechat.viewmodels

import android.util.Log.e
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplechat.repo.Repository
import com.example.simplechat.utils.Resource
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    private val email = MutableLiveData("")
    private val password = MutableLiveData("")
    private val repo = Repository
    val fUser = Firebase.auth.currentUser

    fun setEmail(t: String) {
        email.value = t
    }

    fun setPassword(t: String) {
        password.value = t
    }

    fun loginUser(updateUI: (Resource<FirebaseUser>) -> Unit, onTimeOut: () -> Unit) {
        viewModelScope.launch {
            val job = viewModelScope.launch(Dispatchers.IO) {
                val res = repo.loginUser(email.value!!, password.value!!)
                withContext(Dispatchers.Main) { updateUI(res) }

            }
            // Timeout after 10s
            delay(10000L)
            if (job.isActive) {
                job.cancel()
                e("login", "timeout")
                onTimeOut()
            }

        }

    }


}