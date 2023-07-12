package com.example.simplechat.utils

object ChatId {

    fun createChatId(senderId: String, receiverId: String): String {
        val list = mutableListOf(senderId, receiverId)
        list.sort()
        return "${list[0]}+${list[1]}"
    }


}