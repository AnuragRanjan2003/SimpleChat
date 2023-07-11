package com.example.simplechat.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplechat.repo.Repository
import com.example.simplechat.utils.Resource
import com.example.simplechat.utils.Resource.Success
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    private val email = MutableLiveData<String>("")
    private val password = MutableLiveData<String>("")
    private val repo = Repository

    fun setEmail(t: String) {
        email.value = t
    }

    fun setPassword(t: String) {
        password.value = t
    }

    fun loginUser(updateUI: (user: Resource<FirebaseUser>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = repo.loginUser(email.value!!, password.value!!)
            if (res is Success<FirebaseUser>) {
                withContext(Dispatchers.Main) {
                    updateUI(res)
                }
            }
        }
    }




}