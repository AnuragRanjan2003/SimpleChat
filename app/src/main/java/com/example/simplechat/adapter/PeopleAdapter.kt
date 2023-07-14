package com.example.simplechat.adapter

import android.util.Log.e
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.simplechat.databinding.PeopleLayoutBinding
import com.example.simplechat.models.User

class PeopleAdapter(private val list: ArrayList<User>, private val onClick: (User) -> Unit) :
    RecyclerView.Adapter<PeopleAdapter.MyViewHolder>(), Filterable {

    private var filteredList: ArrayList<User> = list

    inner class MyViewHolder(val binding: PeopleLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            PeopleLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = filteredList.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = filteredList[position]
        holder.binding.itemEmail.text = user.email
        holder.binding.itemName.text = user.name

        holder.itemView.setOnClickListener { onClick(user) }

    }


    fun updateList(newList: ArrayList<User>) {
        list.clear()
        list.addAll(newList)
        filteredList  = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return mFilter
    }

    private val mFilter = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val text = p0.toString()
            val result = FilterResults()
            if (text.isNotBlank()) {
                list.filter { it.name.lowercase().contains(text) }.apply {
                    result.values = this
                    result.count = size
                }
            }else{
                result.values = list
                result.count = list.size
            }



            return result
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            try {
                filteredList = p1?.values as ArrayList<User>
                notifyDataSetChanged()
            } catch (e: ClassCastException) {
                e("filter", e.message.toString())
            }
        }

    }


}