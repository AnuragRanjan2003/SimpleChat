package com.example.simplechat.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Chat(
    val message: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val time: String = "",
)
