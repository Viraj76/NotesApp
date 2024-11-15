
package com.appsv.notesapp.auth.sign_in.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appsv.notesapp.R
import com.appsv.notesapp.auth.AuthViewModel
import com.appsv.notesapp.auth.ViewModelFactoryForActivityContext
import com.appsv.notesapp.core.util.Toasts
import com.appsv.notesapp.core.util.hideDialog
import com.appsv.notesapp.core.util.hidePostDoneDialog
import com.appsv.notesapp.core.util.showDialog
import com.appsv.notesapp.core.util.showSignInDoneDialog
import com.appsv.notesapp.databinding.FragmentSignInBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding

    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, ViewModelFactoryForActivityContext(requireActivity()))[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })

        observeInternetConnection()


        onSignInButtonClicked()
        viewLifecycleOwner.lifecycleScope.launch {
            authViewModel.authResult.collectLatest { userLoggedIn ->
                Log.d("VFJFJFJF", "$userLoggedIn")
                if (userLoggedIn) {
                    val currentUserEmailId = authViewModel.getUserId()
                    if (currentUserEmailId != null) {
                        val bundle = Bundle()
                        bundle.putString("userId", currentUserEmailId)
                        hideDialog()
                        showSignInDoneDialog()
                        delay(1450)
                        hidePostDoneDialog()
                        findNavController().navigate(R.id.action_signInFragment_to_homeFragment, bundle)
                    }
                } else {
                    Log.d("VFJFJFJF", "Error")
                    Toasts.showSimpleToast("SignIn denied!", requireContext())
                    hideDialog()
                }
            }
        }


        return binding.root
    }

    private fun onSignInButtonClicked() {
        binding.signInButton.setOnClickListener {
            showDialog("Signing you.. please wait")
            authViewModel.signInWithGoogle()
        }
    }

    /**
     * Google Sign requires internet connection.
     */
    private fun observeInternetConnection() {
        authViewModel.internetConnectionState()
        lifecycleScope.launch {
            authViewModel.internetState.collect{internetConnected->

                if(internetConnected){
                    binding.clNoInternet.visibility = View.GONE
                    binding.clSignIn.visibility = View.VISIBLE
                }
                else{
                    binding.clNoInternet.visibility = View.VISIBLE
                    binding.clSignIn.visibility = View.GONE
                }
            }
        }

    }
}
