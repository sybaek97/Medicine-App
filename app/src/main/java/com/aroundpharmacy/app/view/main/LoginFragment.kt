package com.aroundpharmacy.app.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aroundpharmacy.app.R
import com.aroundpharmacy.app.databinding.FragmentLoginBinding
import com.aroundpharmacy.app.view.BaseFragment
import com.aroundpharmacy.app.view.home.HomeActivity
import com.aroundpharmacy.app.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment( ) : BaseFragment(){
    override  var isBackAvailable: Boolean = false
    private lateinit var binding : FragmentLoginBinding
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var id : String
    private lateinit var pw : String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnLogin.setOnClickListener {
            id = binding.editEmail.text.toString()
            pw = binding.editPw.text.toString()

            authViewModel.login(id,pw)
        }

        binding.btnSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        authViewModel.loginResult.observe(viewLifecycleOwner){result->

            if(result.isSuccess){
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }else{
                Toast.makeText(requireContext(),"입력하신 정보가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            }

        }

    }
}