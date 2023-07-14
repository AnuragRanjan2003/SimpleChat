package com.example.simplechat.ui.fragments

import android.os.Bundle
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplechat.R
import com.example.simplechat.adapter.PeopleAdapter
import com.example.simplechat.databinding.FragmentPeopleBinding
import com.example.simplechat.viewmodels.PeopleViewModel


class PeopleFragment : Fragment() {
    private lateinit var binding: FragmentPeopleBinding
    private lateinit var adapter: PeopleAdapter
    private val viewModel: PeopleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPeopleBinding.inflate(inflater, container, false)

        adapter = PeopleAdapter(ArrayList()) {
            binding.etSearch.text?.clear()
            val bundle = Bundle()
            bundle.putString("uid", it.uid)
            findNavController().navigate(R.id.action_peopleFragment_to_chatFragment, args = bundle)
        }


        binding.rec.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rec.hasFixedSize()
        binding.rec.adapter = adapter

        viewModel.startGettingUserData()
        viewModel.startGettingAllUsers()

        viewModel.getUser().observe(viewLifecycleOwner) {
            e("get user", "$it")
        }

        viewModel.getUserList().observe(viewLifecycleOwner) {
            e("user list", "$it")
            adapter.updateList(it)
        }

        binding.fabProfile.setOnClickListener {
            binding.etSearch.text?.clear()
            findNavController().navigate(R.id.action_peopleFragment_to_profileFragment)
        }

        binding.etSearch.doAfterTextChanged {
            adapter.filter.filter(it.toString())
        }



        return binding.root
    }


}