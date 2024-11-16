package com.appsv.notesapp.notes.presentation.home.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.appsv.notesapp.R
import com.appsv.notesapp.core.presentation.ViewModelFactoryForActivityContext
import com.appsv.notesapp.core.utils.hideConfirmationDialog
import com.appsv.notesapp.core.utils.hideUserPopup
import com.appsv.notesapp.core.utils.showConfirmationDialog
import com.appsv.notesapp.core.utils.showUserPopup
import com.appsv.notesapp.databinding.FragmentHomeBinding
import com.appsv.notesapp.notes.presentation.home.presentation.adapter.NotesAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var notesAdapter: NotesAdapter

    private val homeViewModel: HomeViewModel by viewModels {
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

        setupFabScrollBehavior()

        onLogOutIconClick()

        observeLogOutDialogState()

        onUserImageClick()

        collectUserPopUpWindowState()

        onDeviceBackPress()

        onNoteAddButtonClick()

        return binding.root
    }

    private fun onDeviceBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })
    }


    private fun onUserImageClick() {
        binding.ivUserImage.setOnClickListener{
            homeViewModel.showUsersPopUpWindow()
        }
    }

    private fun collectUserPopUpWindowState() {
        lifecycleScope.launch {
            homeViewModel.showUsersPopUpWindow.collectLatest{show->
                if(show){
                    showUsersPopUpWindow()
                }
                else{
                    hideUserPopup()
                }
            }
        }
    }

    private suspend  fun showUsersPopUpWindow() {
        homeViewModel.allLoggedInUsers.collect{allUsers->
            showUserPopup(binding.ivUserImage,allUsers,homeViewModel.currentUser.value!!){selectedUser->
                homeViewModel.hideUsersPopUpWindow()
                if(homeViewModel.currentUser.value != selectedUser){
                    homeViewModel.changeAccount(selectedUser)
                    lifecycleScope.launch { getUserByEmailId(selectedUser) }
                }
            }
        }
    }

    private fun observeLogOutDialogState() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.logOutDialogState.collect{state->
                if(state){
                    showConfirmationDialog(
                        icon = R.drawable.baseline_logout_24,
                        iconTint = R.color.red,
                        title = "Log Out",
                        message = "Are you sure you want to log out?",
                        positiveText = "Yes",
                        negativeText = "No",
                        positiveAction = {
                            homeViewModel.onLoggingOutUser()
                            findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
                        },
                        negativeAction = {homeViewModel.hideLogOutConfirmationDialog()}
                    )
                }
                else{
                    hideConfirmationDialog()
                }
            }
        }

    }

    private fun setupFabScrollBehavior() {
        binding.rvShowAllNotes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var lastScrollY = 0
            private val SCROLL_THRESHOLD = 5

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                lastScrollY += dy

                if (abs(lastScrollY) > SCROLL_THRESHOLD) {
                    if (lastScrollY > 0) {
                        if (binding.fabAddNote.isShown) {
                            binding.fabAddNote.hide()
                        }
                    } else {
                        if (!binding.fabAddNote.isShown) {
                            binding.fabAddNote.show()
                        }
                    }
                    lastScrollY = 0
                }
            }
        })
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
                    getUserByEmailId(userId)
                } ?: Log.e("UserInfoError", "User ID is null")
            }
        }
    }

    private suspend  fun getUserByEmailId(userId: String) {
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
    }

    private fun onLogOutIconClick() {
        binding.logoutIcon.setOnClickListener {
            homeViewModel.showLogOutConfirmationDialog()
        }
    }

}