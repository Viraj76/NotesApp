package com.appsv.notesapp.notes.home.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.appsv.notesapp.R
import com.appsv.notesapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val idToken = arguments?.getString("idToken")
        val displayName = arguments?.getString("displayName")

        // Display or use user information
        Toast.makeText(requireContext(), "Welcome, $displayName", Toast.LENGTH_SHORT).show()
        return binding.root
    }
}