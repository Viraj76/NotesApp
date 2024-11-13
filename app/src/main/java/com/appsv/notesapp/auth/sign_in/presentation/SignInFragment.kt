
package com.appsv.notesapp.auth.sign_in.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.appsv.notesapp.auth.AuthViewModel
import com.appsv.notesapp.auth.AuthViewModelFactory
import com.appsv.notesapp.databinding.FragmentSignInBinding
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

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

        authViewModel.authResult.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                authViewModel.setLoggedIn(true)
                Log.i("SignInFragment", "ID Token: ${result.idToken}")
                Log.i("SignInFragment", "Given Name: ${result.givenName}")
                Log.i("SignInFragment", "ID: ${result.id}")
                Log.i("SignInFragment", "Display Name: ${result.displayName}")
                Toast.makeText(requireContext(), "Welcome, ${result.displayName}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Sign-in was canceled or failed", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}