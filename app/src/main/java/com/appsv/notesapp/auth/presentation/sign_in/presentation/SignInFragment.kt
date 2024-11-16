package com.appsv.notesapp.auth.presentation.sign_in.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appsv.notesapp.R
import com.appsv.notesapp.auth.presentation.common.AuthViewModel
import com.appsv.notesapp.core.presentation.ViewModelFactoryForActivityContext
import com.appsv.notesapp.core.utils.Toasts
import com.appsv.notesapp.core.utils.hideSignInWaitDialog
import com.appsv.notesapp.core.utils.hideSignInDoneDialog
import com.appsv.notesapp.core.utils.showSignInWaitDialog
import com.appsv.notesapp.core.utils.showSignInDoneDialog
import com.appsv.notesapp.databinding.FragmentSignInBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactoryForActivityContext(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        onDeviceBackPress()

        collectInternetConnectionState()

        navigateToHome()

        collectSignInWaitDialogState()

        collectSignInDoneDialogState()

        onSignInButtonClicked()

        collectGoogleSignInState()

        return binding.root
    }

    private fun collectGoogleSignInState() {
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.authResult.collectLatest { userLoggedIn ->
                if (userLoggedIn) {
                    authViewModel.hideSignInWaitDialog()
                    authViewModel.showSignInDoneDialog()
                    delay(1400)
                    authViewModel.hideSignInDoneDialog()
                    authViewModel.navigateToHome()
                } else {
                    Toasts.showSimpleToast("SignIn denied! Please try again", requireContext())
                    authViewModel.hideSignInWaitDialog()
                }
            }
        }
    }

    private fun onDeviceBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })
    }

    private fun navigateToHome() {
        lifecycleScope.launch {
            authViewModel.navigateToHome.collect { navigate ->
                if (navigate) {
                    findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                }
            }
        }
    }

    private fun collectSignInDoneDialogState() {
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.signInDoneDialog.collect { state ->
                if (state) {
                    showSignInDoneDialog()
                } else {
                    hideSignInDoneDialog()
                }
            }

        }
    }

    private fun collectSignInWaitDialogState() {
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.signInWaitDialog.collect { state ->
                if (state) {
                    showSignInWaitDialog("Signing you.. please wait")
                } else {
                    hideSignInWaitDialog()
                }
            }
        }
    }

    private fun onSignInButtonClicked() {
        binding.signInButton.setOnClickListener {
            authViewModel.showSignInWaitDialog()
            authViewModel.signInWithGoogle()
        }
    }

    /**
     * Google Sign requires internet connection.
     */
    private fun collectInternetConnectionState() {
        authViewModel.internetConnectionState()
        lifecycleScope.launch {
            authViewModel.internetState.collect { internetConnected ->

                if (internetConnected) {
                    binding.clNoInternet.visibility = View.GONE
                    binding.clSignIn.visibility = View.VISIBLE
                } else {
                    binding.clNoInternet.visibility = View.VISIBLE
                    binding.clSignIn.visibility = View.GONE
                }
            }
        }

    }
}


