package com.example.simplechat.viewmodels

import android.util.Log.e
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplechat.models.User
import com.example.simplechat.repo.UserRepo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PeopleViewModel : ViewModel() {

    private var user = MutableLiveData<User>(User("", "", uid = ""))
    private val userList: MutableLiveData<ArrayList<User>> by lazy { MutableLiveData<ArrayList<User>>() }
    private val fUser = Firebase.auth.currentUser!!

    fun startGettingUserData(uid: String = fUser.uid) {
        viewModelScope.launch(Dispatchers.IO) {
            UserRepo.userStream(uid).collect { snap ->
                withContext(Dispatchers.Main) {
                    user.value = snap.toObject(User::class.java)
                }
            }
        }
    }

    fun startGettingAllUsers(uid: String = fUser.uid) {
        viewModelScope.launch(Dispatchers.IO) {
            UserRepo.allUsersStream(uid).collect { snap ->
                withContext(Dispatchers.Main) {
                    val list = ArrayList<User>()
                    if (!snap.isEmpty) {
                        snap.documents.forEach { it ->
                            if (it.exists()) try {
                                e("user", "${it.toObject(User::class.java)}")
                                list.add(it.toObject(User::class.java)!!)
                            } catch (e: Exception) {
                                e("conversion", e.message.toString())
                            }
                        }
                        userList.value = list
                    }
                }
            }
        }
    }



    fun getUser(): LiveData<User> = user
    fun getUserList(): LiveData<ArrayList<User>> = userList


}