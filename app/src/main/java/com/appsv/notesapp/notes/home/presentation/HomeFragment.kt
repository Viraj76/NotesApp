package com.appsv.notesapp.notes.home.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.appsv.notesapp.R
import com.appsv.notesapp.auth.ViewModelFactoryForActivityContext
import com.appsv.notesapp.databinding.FragmentHomeBinding
import com.appsv.notesapp.notes.home.presentation.adapter.NotesAdapter
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var notesAdapter: NotesAdapter
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this, ViewModelFactoryForActivityContext(requireActivity()))[HomeViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        makeStaggeredViewRecyclerView()

        val currentUserId = homeViewModel.getUserId()
        onLogOutIconClick()

//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
////                if(!currentUserId.isNullOrEmpty()){
////                    findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
////                }
//
//            }
//        })

        getLoggedInUserInfo(currentUserId)

        getNotesAndShow(currentUserId)

        onNoteAddButtonClick()

        return binding.root
    }

    private fun makeStaggeredViewRecyclerView() {
        val staggeredGridLayoutManager = (StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL))
        binding.rvShowAllNotes.layoutManager=staggeredGridLayoutManager
    }

    private fun onNoteAddButtonClick() {
        binding.fabAddNote.setOnClickListener{

            findNavController().navigate(R.id.action_homeFragment_to_addOrEditFragment)
        }
    }

    private fun getNotesAndShow(currentUserId: String?) {

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.getNotesByEmailId(currentUserId!!)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycleScope.launch{
                homeViewModel.notes.collect { state ->
                    when {
                        state.isLoading -> {
                            Log.d("NotesTAG", "loading..")
                            binding.progressIndicator.visibility = View.VISIBLE
                            binding.tvNoNotesText.visibility = View.GONE
                            binding.rvShowAllNotes.visibility = View.GONE
                        }
                        state.notesList.isEmpty() -> {
                            Log.d("NotesTAG", "empty")
                            binding.progressIndicator.visibility = View.GONE
                            binding.tvNoNotesText.visibility = View.VISIBLE
                            binding.rvShowAllNotes.visibility = View.GONE
                        }
                        else -> {
                            Log.d("NotesTAG", "list")
                            binding.progressIndicator.visibility = View.GONE
                            binding.tvNoNotesText.visibility = View.GONE
                            binding.rvShowAllNotes.visibility = View.VISIBLE
                            notesAdapter = NotesAdapter()
                            binding.rvShowAllNotes.adapter = notesAdapter
                            notesAdapter.differ.submitList(state.notesList)
                            // Set up or update RecyclerView adapter with the notesList
                            // notesAdapter.submitList(state.notesList)
                        }
                    }
                }
            }
        }




    }

    private fun getLoggedInUserInfo(currentUserId: String?) {
        viewLifecycleOwner.lifecycleScope.launch {
            currentUserId?.let { userId ->
                homeViewModel.getUserById(userId).collect { loggedInUserDetail ->
                    loggedInUserDetail?.let {

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
