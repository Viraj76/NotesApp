package com.appsv.notesapp.auth.presentation.splash.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.appsv.notesapp.R
import com.appsv.notesapp.auth.presentation.common.AuthViewModel
import com.appsv.notesapp.core.presentation.ViewModelFactoryForActivityContext
import com.appsv.notesapp.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private var splashHandler: Handler? = null
    private val authViewModel: AuthViewModel by viewModels{
        ViewModelFactoryForActivityContext(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)

        checkLoginStatus()

        return binding.root
    }

    private fun checkLoginStatus() {
        splashHandler = Handler(Looper.getMainLooper())
        splashHandler?.postDelayed({
            val currentUserEmailId = authViewModel.getUserId()
            if (currentUserEmailId != null) {
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
            }
        }, 1700)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isAdded) {
            splashHandler?.removeCallbacksAndMessages(null)
            splashHandler = null
        }
    }
}
