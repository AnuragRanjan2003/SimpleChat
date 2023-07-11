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
import com.example.simplechat.databinding.FragmentSignUpBinding
import com.example.simplechat.viewmodels.SignUpViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SignUpFragment : Fragment() {


    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: SignUpViewModel by viewModels<SignUpViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.signupEmail.doAfterTextChanged { viewModel.setEmail(it.toString()) }
        binding.signupPassword.doAfterTextChanged { viewModel.setPassword(it.toString()) }
        binding.signupName.doAfterTextChanged { viewModel.setName(it.toString()) }



        binding.signupBtn.setOnClickListener {
            if (binding.signupEmail.text.isNullOrBlank()) {
                binding.signupEmail.error = "Email Empty"
                return@setOnClickListener
            }

            if (binding.signupPassword.text.isNullOrBlank()) {
                binding.signupPassword.error = "Password Empty"
                return@setOnClickListener
            }
            if (binding.signupName.text.isNullOrBlank()) {
                binding.signupName.error = "Name Empty"
                return@setOnClickListener
            }


            binding.loading.visibility = View.VISIBLE
            viewModel.createUser {
                binding.loading.visibility = View.INVISIBLE
                findNavController().navigate(R.id.action_signUpFragment_to_peopleFragment)
                Toast.makeText(context, "Logged in successfully", Toast.LENGTH_SHORT).show()
            }
        }

        binding.login.setOnClickListener { findNavController().navigate(R.id.action_signUpFragment_to_loginFragment) }


        return binding.root
    }

}