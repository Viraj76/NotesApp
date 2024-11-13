package com.appsv.notesapp.splash.presentation

import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.appsv.notesapp.R
import com.appsv.notesapp.databinding.FragmentSplashBinding
import android.os.Handler

class SplashFragment : Fragment() {
    private lateinit var binding : FragmentSplashBinding
    private var splashHandler: Handler? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = FragmentSplashBinding.inflate(layoutInflater)
        goToLoginOrHomeFragment()
        return binding.root
    }

    private fun goToLoginOrHomeFragment() {
        splashHandler = Handler(Looper.getMainLooper())
        splashHandler?.postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
        },2500)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(isAdded){
            splashHandler?.removeCallbacksAndMessages(null)
            splashHandler = null
        }
    }
}