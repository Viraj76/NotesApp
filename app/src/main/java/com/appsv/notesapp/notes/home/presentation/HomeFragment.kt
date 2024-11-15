package com.appsv.notesapp.notes.home.presentation

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.appsv.notesapp.R
import com.appsv.notesapp.auth.ViewModelFactoryForActivityContext
import com.appsv.notesapp.base.MainActivity
import com.appsv.notesapp.databinding.FragmentHomeBinding
import com.appsv.notesapp.notes.home.presentation.adapter.NotesAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.core.component.getScopeId

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var notesAdapter: NotesAdapter


    private val homeViewModel: HomeViewModel by viewModels{
        ViewModelFactoryForActivityContext(requireActivity())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        getLoggedInUserInfo()
        getNotesAndShow()
        makeStaggeredViewRecyclerView()
        onLogOutIconClick()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })

        onNoteAddButtonClick()

        return binding.root
    }






    private fun makeStaggeredViewRecyclerView() {
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.rvShowAllNotes.layoutManager = staggeredGridLayoutManager
    }

    private fun onNoteAddButtonClick() {
        binding.fabAddNote.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addOrEditFragment)
        }
    }

    private fun getNotesAndShow() {

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.notes.collect { state ->
                when {
                    state.isLoading -> {
                        binding.shimmer.visibility = View.VISIBLE
                        binding.tvNoNotesText.visibility = View.GONE
                        binding.rvShowAllNotes.visibility = View.GONE
                    }
                    state.notesList.isEmpty() -> {
                        binding.shimmer.visibility = View.GONE
                        binding.tvNoNotesText.visibility = View.VISIBLE
                        binding.rvShowAllNotes.visibility = View.GONE
                    }
                    else -> {
                        binding.shimmer.visibility = View.GONE
                        binding.tvNoNotesText.visibility = View.GONE
                        binding.rvShowAllNotes.visibility = View.VISIBLE
                        notesAdapter = NotesAdapter()
                        binding.rvShowAllNotes.adapter = notesAdapter
                        notesAdapter.differ.submitList(state.notesList)
                    }
                }
            }
        }
    }

    private fun getLoggedInUserInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.currentUser.collect { userId ->
                userId?.let {
                    homeViewModel.getUserById(userId).collect { loggedInUserDetail ->
                        loggedInUserDetail?.let {
                            val userName = it.displayName
                            binding.tvUserName.text = userName
                            val checkProfileImage = it.profilePictureUri == null

                            if (!checkProfileImage) {
                                Glide.with(requireActivity())
                                    .load(it.profilePictureUri!!.toUri())
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.google_image)
                                    .into(binding.ivUserImage)
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
            .setPositiveButton("Yes") { _, _ ->
                homeViewModel.onLoggingOutUser()

                findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
            }
            .setNegativeButton("No") { _, _ ->
                alertDialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
}

