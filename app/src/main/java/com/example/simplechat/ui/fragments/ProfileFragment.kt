package com.example.simplechat.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.simplechat.R
import com.example.simplechat.databinding.FragmentProfileBinding
import com.example.simplechat.models.User
import com.example.simplechat.utils.Resource
import com.example.simplechat.utils.Resource.Failure
import com.example.simplechat.utils.Resource.Success
import com.example.simplechat.viewmodels.ProfileViewModel


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.startGettingUserData()
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        viewModel.userLiveData().observe(viewLifecycleOwner) {
            binding.email.text = it.email
            binding.name.setText(it.name)
            binding.id.text = it.uid
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logOut {
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)

            }

        }

        binding.name.doAfterTextChanged { viewModel.editName(it.toString()) }

        viewModel.enableButton().observe(viewLifecycleOwner) { binding.btnUpdate.isEnabled = it }

        binding.btnUpdate.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            viewModel.updateUserName { doAfterUpdate(it) }
        }

        binding.btnDelete.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            viewModel.deleteAccount {
                doOnDelete(it)
            }
        }




        return binding.root
    }

    private fun doOnDelete(resource: Resource<*>) {
        binding.loading.visibility = View.INVISIBLE
        findNavController().navigate(R.id.action_profileFragment_to_loginFragment)

    }

    private fun doAfterUpdate(res: Resource<User>) {
        if (res is Failure<User>) Toast.makeText(context, res.error, Toast.LENGTH_SHORT).show()
        else if (res is Success<User>) Toast.makeText(
            context,
            "Name changed to ${res.data.name}",
            Toast.LENGTH_SHORT
        ).show()
        binding.name.clearFocus()
        binding.loading.visibility = View.INVISIBLE
    }

}