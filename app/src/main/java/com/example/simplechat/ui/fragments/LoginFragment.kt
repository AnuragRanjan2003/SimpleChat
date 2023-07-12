package com.example.simplechat.ui.fragments

import android.os.Bundle
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.simplechat.R
import com.example.simplechat.databinding.FragmentLoginBinding
import com.example.simplechat.utils.Resource.Failure
import com.example.simplechat.utils.Resource.Success
import com.example.simplechat.viewmodels.LoginViewModel
import com.google.firebase.auth.FirebaseUser


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (viewModel.fUser != null) findNavController().navigate(R.id.action_loginFragment_to_peopleFragment)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)


        binding.signup.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_signUpFragment) }

        binding.loginEmail.doAfterTextChanged { viewModel.setEmail(it.toString()) }
        binding.loginPassword.doAfterTextChanged { viewModel.setPassword(it.toString()) }

        binding.btnLogin.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            if (binding.loginEmail.text.isNullOrBlank()) {
                binding.loginEmail.error = "Email Empty"
                binding.loading.visibility = View.INVISIBLE
                return@setOnClickListener
            }

            if (binding.loginPassword.text.isNullOrBlank()) {
                binding.loginPassword.error = "Password Empty"
                binding.loading.visibility = View.INVISIBLE
                return@setOnClickListener
            } else viewModel.loginUser(updateUI = {
                if (it is Success<FirebaseUser>) {
                    binding.loading.visibility = View.INVISIBLE
                    Toast.makeText(context, "Logged in Successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_loginFragment_to_peopleFragment)
                } else {
                    Toast.makeText(context, (it as Failure).error, Toast.LENGTH_SHORT)
                        .show()
                    binding.loading.visibility = View.INVISIBLE
                }
            }, onTimeOut = {
                binding.loading.visibility = View.INVISIBLE
                Toast.makeText(
                    context,
                    "Request Timed out, Check your connection",
                    Toast.LENGTH_SHORT
                ).show()
                e("login", "login timeout")
            })
        }


        return binding.root
    }


}