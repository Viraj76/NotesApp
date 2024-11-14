
package com.appsv.notesapp.auth.sign_in.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.appsv.notesapp.R
import com.appsv.notesapp.auth.AuthViewModel
import com.appsv.notesapp.auth.AuthViewModelFactory
import com.appsv.notesapp.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding

    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, AuthViewModelFactory(requireActivity()))[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.signInButton.setOnClickListener {
            authViewModel.signInWithGoogle()
        }

        authViewModel.authResult.observe(viewLifecycleOwner) { userLoggedIn ->
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
}
