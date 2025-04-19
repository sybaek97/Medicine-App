package com.aroundpharmacy.app.view.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aroundpharmacy.app.R
import com.aroundpharmacy.app.databinding.FragmentLoginBinding
import com.aroundpharmacy.app.view.BaseFragment
import com.aroundpharmacy.app.view.home.HomeActivity
import com.aroundpharmacy.app.viewModel.AuthViewModel

class LoginFragment( ) : BaseFragment(){
    override  var isBackAvailable: Boolean = false
    private lateinit var binding : FragmentLoginBinding
    private lateinit var authViewModel : AuthViewModel
    private lateinit var id : String
    private lateinit var pw : String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnLogin.setOnClickListener {
            id = binding.editEmail.toString()
            pw = binding.editPw.toString()

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