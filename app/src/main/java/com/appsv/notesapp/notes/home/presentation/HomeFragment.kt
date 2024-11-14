package com.appsv.notesapp.notes.home.presentation

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.appsv.notesapp.R
import com.appsv.notesapp.auth.ViewModelFactoryForActivityContext
import com.appsv.notesapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this, ViewModelFactoryForActivityContext(requireActivity()))[HomeViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        val currentUserId = arguments?.getString("userId")
        onLogOutIconClick(currentUserId)
        Toast.makeText(requireContext(), "Welcome, $currentUserId", Toast.LENGTH_SHORT).show()
        return binding.root
    }

    private fun onLogOutIconClick(currentUserId: String?) {
        binding.logoutIcon.setOnClickListener {
            showDialogToConfirm(currentUserId)
        }
    }

    private fun showDialogToConfirm(currentUserId: String?) {
        val builder = AlertDialog.Builder(requireActivity())
        val alertDialog = builder.create()
        builder
            .setTitle("Log Out")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes"){ _, _ ->
                homeViewModel.onLoggingOutUser(currentUserId?:"")
                findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
            }
            .setNegativeButton("No"){ _, _ ->
                alertDialog.dismiss()
            }
            .show()
            .setCancelable(false)
    }
}
