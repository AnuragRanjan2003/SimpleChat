package com.example.simplechat.repo

import com.example.simplechat.models.Chat
import com.example.simplechat.utils.ChatId
import com.example.simplechat.utils.Resource
import com.example.simplechat.utils.SafeCall
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await


object ChatRepo {

    private val db = Firebase.firestore.collection("chats")
    private val dbUser = Firebase.firestore.collection("user")
    private val user = Firebase.auth.currentUser!!

    suspend fun putChat(chat: Chat): Resource<Chat> {
        val id = ChatId.createChatId(chat.senderId, chat.receiverId)
        return SafeCall.fireStore(chat) {
            db.document(id).collection("allChats").document(chat.time).set(chat).await()
        }
    }

    fun getAllChats(id : String): Flow<QuerySnapshot> = db.document(id).collection("allChats").snapshots()

    suspend fun getUser(id : String): DocumentSnapshot? = dbUser.document(id).get().await()


}