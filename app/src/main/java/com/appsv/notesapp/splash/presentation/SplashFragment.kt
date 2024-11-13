package com.appsv.notesapp.splash.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appsv.notesapp.R
import com.appsv.notesapp.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {
    private lateinit var binding : FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding  = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

}