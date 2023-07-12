package com.example.simplechat.viewmodels

import android.util.Log.e
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplechat.models.Chat
import com.example.simplechat.models.User
import com.example.simplechat.repo.ChatRepo
import com.example.simplechat.utils.ChatId
import com.example.simplechat.utils.Resource.Failure
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel : ViewModel() {

    val fUser = Firebase.auth.currentUser!!
    private val receiver: MutableLiveData<User> by lazy { MutableLiveData<User>() }
    private val chats: MutableLiveData<ArrayList<Chat>> by lazy { MutableLiveData<ArrayList<Chat>>() }


    fun getReceiverData(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = ChatRepo.getUser(id)
            res?.apply {
                withContext(Dispatchers.Main) { receiver.value = toObject(User::class.java) }

            }
        }
    }

    fun postChat(chat: Chat, onFail: (String) -> Unit) {
        val res = viewModelScope.async(Dispatchers.IO) { ChatRepo.putChat(chat) }
        viewModelScope.launch(Dispatchers.Main) {
            val result = res.await()
            if (result is Failure<Chat>) onFail(result.error)
        }
    }

    fun startGettingAllChats(receiverID: String) {
        val id = ChatId.createChatId(receiverID, fUser.uid)
        viewModelScope.launch(Dispatchers.IO) {
            ChatRepo.getAllChats(id).collect { snap ->
                if (snap.documents.isNotEmpty() || !snap.isEmpty) {
                    val list = ArrayList<Chat>()
                    snap.documents.forEach {
                        if (it.exists()) try {
                            list.add(it.toObject(Chat::class.java)!!)
                        } catch (e: Exception) {
                            e("conversion", "cant convert to  chat")
                        }

                    }

                    withContext(Dispatchers.Main) { chats.value = list }
                }
            }
        }
    }


    fun getReceiver(): LiveData<User> = receiver
    fun getChats(): LiveData<ArrayList<Chat>> = chats
}