package com.aroundpharmacy.app.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aroundpharmacy.app.databinding.FragmentAroundPharmacyBinding
import com.aroundpharmacy.app.databinding.FragmentMypageBinding
import com.aroundpharmacy.app.databinding.FragmentPharmacySearchBinding
import com.aroundpharmacy.app.view.BaseFragment

class PharmacySearchFragment : BaseFragment(){
    override  var isBackAvailable: Boolean = false
    private lateinit var binding : FragmentPharmacySearchBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPharmacySearchBinding.inflate(inflater, container, false)
        return binding.root
    }

}