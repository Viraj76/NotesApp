
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
import com.appsv.notesapp.databinding.FragmentSignInBinding
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
        binding.signInButton.setOnClickListener {
            authViewModel.signInWithGoogle()
        }

        authViewModel.authResult.observe(viewLifecycleOwner) { userLoggedIn ->
            Log.d("LOGGG", userLoggedIn.toString())
            if(userLoggedIn){
                val currentUserEmailId = authViewModel.getUserId()
                if (currentUserEmailId != null) {
                    val bundle = Bundle()
                    bundle.putString("userId", currentUserEmailId)
                    findNavController().navigate(R.id.action_signInFragment_to_homeFragment,bundle)
                }
            }

        }
        return binding.root
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
