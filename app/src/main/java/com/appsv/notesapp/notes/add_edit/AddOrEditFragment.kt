package com.appsv.notesapp.notes.add_edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.appsv.notesapp.databinding.FragmentAddOrEditBinding

class AddOrEditFragment : Fragment() {

    private lateinit var binding : FragmentAddOrEditBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddOrEditBinding.inflate(layoutInflater)

        return binding.root
    }


}