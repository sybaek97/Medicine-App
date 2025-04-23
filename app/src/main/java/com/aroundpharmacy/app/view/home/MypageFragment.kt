package com.aroundpharmacy.app.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.aroundpharmacy.app.databinding.FragmentMypageBinding
import com.aroundpharmacy.app.view.BaseFragment
import com.aroundpharmacy.app.view.main.MainActivity
import com.aroundpharmacy.app.viewModel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageFragment : BaseFragment(){
    override  var isBackAvailable: Boolean = false
    private lateinit var binding : FragmentMypageBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnLogout.setOnClickListener {
                authViewModel.logout()
            }
            btnUserOut.setOnClickListener {
                authViewModel.deleteUser()
            }
        }

        authViewModel.logoutResult.observe(viewLifecycleOwner){result->

            if (result.isSuccess){
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }else{
                Toast.makeText(requireContext(),"로그아웃에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }

        }
        authViewModel.deleteResult.observe(viewLifecycleOwner){result->

            if (result.isSuccess){
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }else{
                Toast.makeText(requireContext(),"회원탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }

        }

    }




}