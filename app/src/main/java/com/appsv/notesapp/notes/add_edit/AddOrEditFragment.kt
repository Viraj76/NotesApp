package com.appsv.notesapp.notes.add_edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appsv.notesapp.R
import com.appsv.notesapp.core.domain.Notes
import com.appsv.notesapp.core.util.Toasts
import com.appsv.notesapp.core.util.enums.Priority
import com.appsv.notesapp.core.util.getCurrentLocalDateAndTimeInLong
import com.appsv.notesapp.core.util.getUUIDRandomUniqueString

import com.appsv.notesapp.databinding.FragmentAddOrEditBinding
import com.appsv.notesapp.notes.NotesViewModel
import kotlinx.coroutines.launch

/**
* This Fragment is reusable for Add and Edit.
 */
class AddOrEditFragment : Fragment() {

    private lateinit var binding : FragmentAddOrEditBinding
    private var priority: Priority = Priority.LOW
    private val notesViewModel : NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddOrEditBinding.inflate(layoutInflater)

        onBackIconClick()
        setLowPriority()
        onPriorityOvalsClick()
        onAddNoteButtonClicked()

        return binding.root
    }

    private fun onAddNoteButtonClicked() {
        binding.fabDone.setOnClickListener {
            lifecycleScope.launch{
                createNote()
            }
        }
    }

    private  fun createNote() {

        val title = binding.etEditTitle.text.toString()
        val subTitle = binding.etEditSubtitle.text.toString()
        val description = binding.etEditDescription.text.toString()
        val dateAndTime = getCurrentLocalDateAndTimeInLong()
        val currentUserId = notesViewModel.getUserId()

        if(title != ""){
            val data = Notes(
                title = title,
                subTitle = subTitle,
                notes = description,
                date = dateAndTime,
                priority = priority,
                emailId = currentUserId!!
            )
            notesViewModel.saveOrUpdateNote(data)
            Toasts.showSimpleToast("Notes Added Successfully",requireContext())
            findNavController().navigate(R.id.action_addOrEditFragment_to_homeFragment)
        }
        else{
            Toasts.showSimpleToast("Please enter title at least!",requireContext())
        }

    }

    private fun onPriorityOvalsClick() {
        // Low
        binding.ivLowPriority.setOnClickListener {
            priority = Priority.LOW
            binding.ivLowPriority.setImageResource(R.drawable.baseline_done_24)
            binding.ivMediumPriority.setImageResource(0)
            binding.ivHighPriority.setImageResource(0)
            Toasts.showSimpleToast("Priority : Low",requireContext())
        }

        // Medium
        binding.ivMediumPriority.setOnClickListener {
            priority =Priority.MEDIUM
            binding.ivMediumPriority.setImageResource(R.drawable.baseline_done_24)
            binding.ivLowPriority.setImageResource(0)
            binding.ivHighPriority.setImageResource(0)
            Toasts.showSimpleToast("Priority : Medium",requireContext())

        }

        // High
        binding.ivHighPriority.setOnClickListener {
            priority =Priority.HIGH
            binding.ivHighPriority.setImageResource(R.drawable.baseline_done_24)
            binding.ivMediumPriority.setImageResource(0)
            binding.ivLowPriority.setImageResource(0)
            Toasts.showSimpleToast("Priority : High",requireContext())
        }
    }

    private fun setLowPriority() {
        binding.ivLowPriority.setImageResource(R.drawable.baseline_done_24)
    }

    private fun onBackIconClick() {
        binding.ivBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_addOrEditFragment_to_homeFragment)
        }
    }


}