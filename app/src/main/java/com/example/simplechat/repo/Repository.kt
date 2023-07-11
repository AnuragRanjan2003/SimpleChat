package com.example.simplechat.repo

import com.example.simplechat.models.User
import com.example.simplechat.utils.Resource
import com.example.simplechat.utils.Resource.Failure
import com.example.simplechat.utils.Resource.Success
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object Repository {
    private val mAuth = Firebase.auth
    private val db = Firebase.firestore


    suspend fun createUser(email: String, password: String, name: String): Resource<FirebaseUser> {
        return try {
            val res = mAuth.createUserWithEmailAndPassword(email, password).await()
            if (res.user != null) {
                val user = User(name, email, res.user!!.uid)
                val r = saveUser(user)
                if (r is Success<User>)
                    Success<FirebaseUser>(res.user!!)
                else Failure((r as Failure<User>).error)
            } else Failure("something went wrong")
        } catch (e: FirebaseAuthException) {
            Failure(e.message.toString())
        } catch (e: FirebaseException) {
            Failure(e.message.toString())
        }

    }

    suspend fun loginUser(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val res = mAuth.signInWithEmailAndPassword(email, password).await()
            if (res.user != null) Success<FirebaseUser>(res.user!!)
            else Failure("something went wrong")
        } catch (e: FirebaseAuthException) {
            Failure(e.message.toString())
        } catch (e: FirebaseException) {
            Failure(e.message.toString())
        }
    }

    private suspend fun saveUser(user: User): Resource<User> {
        return try {
            db.collection("user").document(user.uid).set(user, SetOptions.merge()).await()
            Success(user)
        } catch (e: FirebaseFirestoreException) {
            Failure(e.message.toString())
        } catch (e: FirebaseException) {
            Failure(e.message.toString())
        }
    }


}