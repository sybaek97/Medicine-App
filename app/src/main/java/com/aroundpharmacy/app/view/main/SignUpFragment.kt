package com.aroundpharmacy.app.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aroundpharmacy.app.R
import com.aroundpharmacy.app.databinding.FragmentLoginBinding
import com.aroundpharmacy.app.databinding.FragmentSignupBinding
import com.aroundpharmacy.app.view.BaseFragment
import com.aroundpharmacy.app.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var pw: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnSignup.setOnClickListener {
                if (editEmail.text.toString().isEmpty() ||
                    editPw.text.toString().isEmpty() ||
                    editName.text.toString().isEmpty() ||
                    editPwCheck.text.toString().isEmpty()
                ) {
                    Toast.makeText(requireContext(), "모든 항목을 입력하세요!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                name = editName.text.toString()
                email = editEmail.text.toString()
                if (editPw.text.toString() == editPwCheck.text.toString()) pw = editPwCheck.text.toString()
                else {
                    Toast.makeText(requireContext(), "입력하신 정보가 올바르지 않습니다.", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                authViewModel.signUp(name,email,pw)
            }

          btnLogin.setOnClickListener {
              findNavController().navigateUp()
          }

        }


        authViewModel.sigUpResult.observe(viewLifecycleOwner){result->
            if (result.isSuccess){
                Toast.makeText(requireContext(), "회원가입 성공!", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)

            }else{
                Toast.makeText(requireContext(), "이메일과 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }


}