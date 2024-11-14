package com.appsv.notesapp.notes.add_edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.appsv.notesapp.R

import com.appsv.notesapp.databinding.FragmentAddOrEditBinding

class AddOrEditFragment : Fragment() {

    private lateinit var binding : FragmentAddOrEditBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddOrEditBinding.inflate(layoutInflater)
        onBackIconClick()
        return binding.root
    }

    private fun onBackIconClick() {
        binding.ivBackButton.setOnClickListener {

            findNavController().navigate(R.id.action_addOrEditFragment_to_homeFragment)
        }
    }


}