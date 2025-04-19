package com.aroundpharmacy.app.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment


abstract class BaseFragment : Fragment() {
    private var backKeyPressedTime: Long = 0
    abstract val isBackAvailable: Boolean

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기 콜백 처리
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                        Toast.makeText(context, "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT)
                            .show()
                        backKeyPressedTime = System.currentTimeMillis()
                        return
                    } else {
                        if (isBackAvailable) {
                            NavHostFragment.findNavController(this@BaseFragment).popBackStack()
                        } else {
                            if (activity != null) {
                                activity!!.finish()
                            }
                        }
                    }
                }
            })
    }
}
