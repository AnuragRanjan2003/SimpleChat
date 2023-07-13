package com.example.simplechat.repo

import com.example.simplechat.models.User
import com.example.simplechat.utils.SafeCall
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

object UserRepo {
    private val db = Firebase.firestore.collection("user")
    private val mAuth = Firebase.auth


    suspend fun updateName(uid: String, name: String) =
        SafeCall.fireStore(User(name = name, "", uid = uid)) {
            db.document(uid).update("name", name).await()
        }


    fun userStream(uid: String): Flow<DocumentSnapshot> = db.document(uid).snapshots()


    fun allUsersStream(uid: String) = db.whereNotEqualTo("uid", uid).snapshots()

    suspend fun deleteUser(user: User) {
        val res = mAuth.signInWithEmailAndPassword(user.email, user.password).await()
        if (res.user != null) mAuth.currentUser?.delete()?.await()
    }

    suspend fun deleteUserData(uid: String): Void = db.document(uid).delete().await()


}