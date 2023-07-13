package com.example.simplechat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplechat.models.User
import com.example.simplechat.repo.UserRepo
import com.example.simplechat.utils.Resource
import com.example.simplechat.utils.Resource.Failure
import com.example.simplechat.utils.SafeCall
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel : ViewModel() {
    private val mAuth = Firebase.auth

    private val user: MutableLiveData<User> by lazy { MutableLiveData<User>() }
    private val editedName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val enableUpdate = MutableLiveData<Boolean>(false)

    fun startGettingUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            UserRepo.userStream(mAuth.currentUser!!.uid).collect {
                if (it.data != null) {
                    withContext(Dispatchers.Main) {
                        user.value = it.toObject(User::class.java)
                        editedName.value = user.value?.name
                    }
                }
            }
        }
    }

    fun updateUserName(onComplete: (Resource<User>) -> Unit) {
        val res = viewModelScope.async(Dispatchers.IO) {
            UserRepo.updateName(
                mAuth.currentUser!!.uid,
                editedName.value.toString()
            )
        }
        viewModelScope.launch(Dispatchers.Main) { onComplete(res.await()) }
    }


    fun logOut(onComplete: () -> Unit) {
        mAuth.signOut()
        onComplete()
    }

    fun deleteAccount(onComplete: (Resource<*>) -> Unit) {
        val user = user.value
        val res = viewModelScope.async(Dispatchers.IO) {
            if (mAuth.currentUser == null || user == null) Failure("null User")
            else {
                val uid = mAuth.currentUser!!.uid
                val res = SafeCall.auth { UserRepo.deleteUser(user) }
                if (res is Resource.Success<Unit>) {
                    SafeCall.fireStore(res.data) {
                        UserRepo.deleteUserData(
                            uid
                        )
                    }
                } else Failure((res as Failure<*>).error)
            }
        }

        viewModelScope.launch {
            val result = res.await()
            onComplete(result)
        }
    }

    fun editName(t: String) {
        editedName.value = t
        enableUpdate.value = editedName.value != user.value?.name && t.isNotBlank()
    }


    fun userLiveData(): LiveData<User> = user

    fun enableButton(): LiveData<Boolean> = enableUpdate


}