package com.example.simplechat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplechat.databinding.PeopleLayoutBinding
import com.example.simplechat.models.User

class PeopleAdapter(private val list: ArrayList<User>) :
    RecyclerView.Adapter<PeopleAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: PeopleLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            PeopleLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = list[position]
        holder.binding.itemEmail.text = user.email
        holder.binding.itemName.text = user.name
    }

    fun addUser(user: User) {
        val l = list.size
        list.add(user)
        notifyItemInserted(l)
    }

    fun updateList(newList: ArrayList<User>) {
        val l = list.size
        list.clear()
        list.addAll(newList)
        notifyItemRangeChanged(0, newList.size)
    }

}