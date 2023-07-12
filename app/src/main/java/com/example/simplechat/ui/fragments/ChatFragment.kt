package com.example.simplechat.ui.fragments

import android.os.Bundle
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplechat.adapter.ChatAdapter
import com.example.simplechat.databinding.FragmentChatBinding
import com.example.simplechat.models.Chat
import com.example.simplechat.viewmodels.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Locale


class ChatFragment : Fragment() {

    private var receiverID: String? = null
    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var adapter: ChatAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            receiverID = it.getString("uid")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater)
        e("user", receiverID.toString())


        adapter = ChatAdapter(ArrayList(), viewModel.fUser.uid)


        if (!receiverID.isNullOrBlank()) {
            viewModel.getReceiverData(receiverID!!)
            viewModel.startGettingAllChats(receiverID!!)
        }
        viewModel.getReceiver().observe(viewLifecycleOwner) {
            binding.email.text = it.email
            binding.name.text = it.name
        }

        viewModel.getChats().observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }



        binding.rec.adapter = adapter
        binding.rec.hasFixedSize()
        binding.rec.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.btnSend.setOnClickListener {
            if (binding.etChat.text.isNullOrBlank()) return@setOnClickListener
            if (receiverID.isNullOrBlank()) return@setOnClickListener
            val chat = Chat(
                message = binding.etChat.text.toString(),
                senderId = viewModel.fUser.uid,
                receiverId = receiverID!!,
                time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                    java.util.Date()
                )
            )
            viewModel.postChat(chat) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
            binding.etChat.text?.clear()
        }



        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString("uid", param1)
                }
            }
    }
}