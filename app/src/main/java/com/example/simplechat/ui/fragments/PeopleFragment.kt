package com.example.simplechat.ui.fragments

import android.os.Bundle
import android.util.Log.e
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplechat.R
import com.example.simplechat.adapter.PeopleAdapter
import com.example.simplechat.databinding.FragmentPeopleBinding
import com.example.simplechat.models.User
import com.example.simplechat.viewmodels.PeopleViewModel


class PeopleFragment : Fragment() {
    private lateinit var binding: FragmentPeopleBinding
    private val adapter  = PeopleAdapter(ArrayList<User>())
    private val viewModel : PeopleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPeopleBinding.inflate(inflater , container , false)

        binding.rec.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.rec.hasFixedSize()
        binding.rec.adapter = adapter

        viewModel.startGettingUserData()
        viewModel.startGettingAllUsers()

        viewModel.getUser().observe(viewLifecycleOwner){
            e("get user" , "$it")
        }

        viewModel.getUserList().observe(viewLifecycleOwner){
            e("user list", "$it")
            adapter.updateList(it)
        }




        return binding.root
    }


}