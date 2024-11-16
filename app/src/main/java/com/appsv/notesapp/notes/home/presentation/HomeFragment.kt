package com.appsv.notesapp.notes.home.presentation

import android.app.AlertDialog
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
import com.appsv.notesapp.auth.ViewModelFactoryForActivityContext
import com.appsv.notesapp.core.util.hideConfirmationDialog
import com.appsv.notesapp.core.util.hideUserPopup
import com.appsv.notesapp.core.util.showConfirmationDialog
import com.appsv.notesapp.core.util.showUserPopup
import com.appsv.notesapp.databinding.FragmentHomeBinding
import com.appsv.notesapp.notes.home.presentation.adapter.NotesAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var notesAdapter: NotesAdapter
    private var isScrollingUp = false
    private var lastFirstVisibleItem = 0

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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })

        onNoteAddButtonClick()

        return binding.root
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

//    @SuppressLint("MissingInflatedId")
//    private fun showUserPopup(view: View, userList: List<LoggedInUserDetail?>) {
//        val popupView = LayoutInflater.from(context).inflate(R.layout.popup_user_list, null)
//        usersPopUpWindow = PopupWindow(
//            popupView,
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            true
//        )
//
//        val userContainer: LinearLayout = popupView.findViewById(R.id.userContainer)
//
//        userList.forEach { user ->
//            val userView = LayoutInflater.from(requireContext()).inflate(R.layout.item_loggedin_users, userContainer, false)
//
//            // Bind user data
//            val userImage: ImageView = userView.findViewById(R.id.ivUserImage)
//            val userName: TextView = userView.findViewById(R.id.tvUserName)
//            val userEmail: TextView = userView.findViewById(R.id.tvUserEmail)
//
//            userName.text = user?.displayName
//            userEmail.text = user?.id
//            Glide.with(this).load(user?.profilePictureUri?.toUri()).into(userImage)
//
//            // Add click listener for each user
//            userView.setOnClickListener {
//                Toast.makeText(requireContext(), "Selected: ${user?.displayName}", Toast.LENGTH_SHORT).show()
//                homeViewModel.hideUsersPopUpWindow()
//            }
//
//            userContainer.addView(userView)
//        }
//
//        usersPopUpWindow.elevation = 10f
//        usersPopUpWindow.showAsDropDown(view, 0, 10) // Show below the anchor view
//    }


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
            private val SCROLL_THRESHOLD = 5 // Lower value = more sensitive. Adjust this value to change sensitivity

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Track vertical scroll amount
                lastScrollY += dy

                // Check if absolute scroll amount exceeds threshold
                if (Math.abs(lastScrollY) > SCROLL_THRESHOLD) {
                    if (lastScrollY > 0) {
                        // Scrolling down
                        if (binding.fabAddNote.isShown) {
                            binding.fabAddNote.hide()
                        }
                    } else {
                        // Scrolling up
                        if (!binding.fabAddNote.isShown) {
                            binding.fabAddNote.show()
                        }
                    }
                    // Reset scroll tracker after handling
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

    suspend  fun getUserByEmailId(userId: String) {
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

    private fun showDialogToConfirm() {
        val builder = AlertDialog.Builder(requireActivity())
        val alertDialog = builder.create()

        builder
            .setIcon(R.drawable.baseline_logout_24)
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