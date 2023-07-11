package com.example.simplechat.repo

import com.example.simplechat.models.User
import com.google.android.play.core.integrity.e
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

object UserRepo {
    private val db = Firebase.firestore.collection("user")

    fun  userStream(uid : String): Flow<DocumentSnapshot> = db.document(uid).snapshots()


    fun  allUsersStream(uid : String) = db.whereNotEqualTo("uid",uid).snapshots()



}