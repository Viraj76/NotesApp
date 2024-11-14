package com.appsv.notesapp.notes.home.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import com.appsv.notesapp.R
import com.appsv.notesapp.auth.ViewModelFactoryForActivityContext
import com.appsv.notesapp.databinding.FragmentHomeBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        onLogOutIconClick()

        getLoggedInUserInfo(currentUserId)


        Toast.makeText(requireContext(), "Welcome, $currentUserId", Toast.LENGTH_SHORT).show()
        return binding.root
    }

    private fun getLoggedInUserInfo(currentUserId: String?) {
        viewLifecycleOwner.lifecycleScope.launch {
            currentUserId?.let { userId ->
                homeViewModel.getUserById(userId).collect { loggedInUserDetail ->
                    loggedInUserDetail?.let {
                        Log.d("UserInfo", it.toString())

                        val userName = it.displayName
                        binding.tvUserName.text = userName
                        val checkProfileImage = it.profilePictureUri == null

                        if (!checkProfileImage) {
                            binding.ivUserImage.visibility = View.VISIBLE
                            lifecycleScope.launch {
                                Glide.with(requireActivity())
                                    .load(it.profilePictureUri!!.toUri())
                                    .placeholder(R.drawable.google_image)
                                    .into(binding.ivUserImage)
                            }
                            binding.tvUserImage.visibility = View.GONE
                        } else {
                            binding.ivUserImage.visibility = View.GONE
                            binding.tvUserImage.visibility = View.VISIBLE
                            binding.tvUserImage.text = userName?.get(0)?.toString() ?: ""
                        }
                    }
                }
            } ?: Log.e("UserInfoError", "User ID is null")
        }
    }


    private fun onLogOutIconClick() {
        binding.logoutIcon.setOnClickListener {
            showDialogToConfirm()
        }
    }

    private fun showDialogToConfirm() {
        val builder = AlertDialog.Builder(requireActivity())
        val alertDialog = builder.create()
        builder
            .setTitle("Log Out")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes"){ _, _ ->
                homeViewModel.onLoggingOutUser()
                findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
            }
            .setNegativeButton("No"){ _, _ ->
                alertDialog.dismiss()
            }
            .show()
            .setCancelable(false)
    }
}
