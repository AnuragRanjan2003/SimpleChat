package com.example.simplechat.viewmodels

import android.util.Log.e
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplechat.repo.Repository
import com.example.simplechat.utils.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpViewModel : ViewModel() {
    val repo = Repository
    private val email = MutableLiveData("")
    private val password = MutableLiveData("")
    private val name = MutableLiveData("")


    fun setEmail(t: String) {
        email.value = t
    }

    fun setPassword(t: String) {
        password.value = t
    }

    fun setName(t: String) {
        name.value = t
    }

    fun createUser(updateUI: (Resource<FirebaseUser>) -> Unit, timeOut: () -> Unit) {
        viewModelScope.launch {
            val job = viewModelScope.launch(Dispatchers.IO) {
                val res = repo.createUser(email.value!!, password.value!!, name.value!!)
                withContext(Dispatchers.Main) {
                    updateUI(res)
                }
            }
            delay(10000L)
            if (job.isActive) {
                e("signup", "request Cancelled")
                timeOut()
            }

        }
    }

    fun getEmail(): LiveData<String> = email
    fun getPassword(): LiveData<String> = password
    fun getName(): LiveData<String> = name

}