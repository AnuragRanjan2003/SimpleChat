package com.example.simplechat.repo

import com.example.simplechat.models.User
import com.example.simplechat.utils.Resource
import com.example.simplechat.utils.SafeCall
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object Repository {
    private val mAuth = Firebase.auth
    private val db = Firebase.firestore


    suspend fun createUser(email: String, password: String, name: String): Resource<FirebaseUser> =
        SafeCall.auth {
            mAuth.createUserWithEmailAndPassword(email, password).await().user?.apply {
                val user = User(name, email,password, uid = uid)
                saveUser(user)
            }


        }

    suspend fun loginUser(email: String, password: String): Resource<FirebaseUser> =
        SafeCall.auth { mAuth.signInWithEmailAndPassword(email, password).await().user }


    private suspend fun saveUser(user: User): Resource<User> = SafeCall.fireStore(user) {
        db.collection("user").document(user.uid).set(user, SetOptions.merge()).await()
    }


}