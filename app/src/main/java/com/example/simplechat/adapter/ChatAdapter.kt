package com.example.simplechat.adapter

import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplechat.databinding.ReceiverChatLayoutBinding
import com.example.simplechat.databinding.SenderChatLayoutBinding
import com.example.simplechat.models.Chat
import java.text.SimpleDateFormat
import java.util.Locale

const val SENDER_VIEW_TYPE = 0
const val RECEIVER_VIEW_TYPE = 1
const val SENDER_WITH_DATE_TYPE = 2
const val RECEIVER_WITH_DATE_TYPE = 3

class ChatAdapter(private val list: ArrayList<Chat>, private val senderId: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private inner class SenderViewHolder(
        val binding: SenderChatLayoutBinding,
        val showDate: Boolean
    ) :
        RecyclerView.ViewHolder(binding.root)

    private inner class ReceiverViewHolder(
        val binding: ReceiverChatLayoutBinding,
        val showDate: Boolean
    ) :
        RecyclerView.ViewHolder(binding.root)


    override fun getItemViewType(position: Int): Int {
        val chat = list[position]
        if (chat.senderId == this.senderId) {
            if (position != 0) {
                val lastChat = list[position - 1]
                return if (getDate(chat.time) != getDate(lastChat.time)) SENDER_WITH_DATE_TYPE
                else SENDER_VIEW_TYPE
            }
            return SENDER_WITH_DATE_TYPE
        } else {
            if (position != 0) {
                val lastChat = list[position - 1]
                return if (getDate(chat.time) != getDate(lastChat.time)) RECEIVER_WITH_DATE_TYPE
                else RECEIVER_VIEW_TYPE
            }

            return RECEIVER_WITH_DATE_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val senderBinding =
            SenderChatLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val receiverBinding =
            ReceiverChatLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return when (viewType) {
            SENDER_VIEW_TYPE -> SenderViewHolder(senderBinding, false)
            RECEIVER_VIEW_TYPE -> ReceiverViewHolder(receiverBinding, false)
            SENDER_WITH_DATE_TYPE -> SenderViewHolder(senderBinding, true)
            RECEIVER_WITH_DATE_TYPE -> ReceiverViewHolder(receiverBinding, true)
            else -> SenderViewHolder(senderBinding, false)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SenderViewHolder) bindSender(holder.binding, list[position], holder.showDate)
        else if (holder is ReceiverViewHolder) bindReceiver(holder.binding, list[position] , holder.showDate)


    }


    private fun bindSender(binding: SenderChatLayoutBinding, chat: Chat, showDate: Boolean) {
        e("show date","$showDate")
        binding.chat.text = chat.message
        val date = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).parse(chat.time)
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
        binding.time.text = time
        binding.date.date.text = getDisplayDate(chat.time)
        if (showDate) binding.date.root.visibility = View.VISIBLE
    }

    private fun bindReceiver(binding: ReceiverChatLayoutBinding, chat: Chat, showDate: Boolean) {
        binding.chat.text = chat.message
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(chat.time)
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
        e("time :", time)
        binding.time.text = time
        binding.date.date.text = getDisplayDate(chat.time)
        if (showDate) binding.date.root.visibility = View.VISIBLE
    }

    fun updateList(newList: ArrayList<Chat>) {
        list.clear()
        list.addAll(newList)
        notifyItemRangeChanged(0, newList.size)
    }

    private fun getDate(time: String): String {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(time)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
    }

    private fun getDisplayDate(time: String): String {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(time)
        return SimpleDateFormat("dd MMM", Locale.getDefault()).format(date)
    }


}