package com.appsv.notesapp.notes.add_edit

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appsv.notesapp.R
import com.appsv.notesapp.core.domain.Notes
import com.appsv.notesapp.core.util.Toasts
import com.appsv.notesapp.core.util.enums.Priority
import com.appsv.notesapp.core.util.getCurrentLocalDateAndTimeInLong

import com.appsv.notesapp.databinding.FragmentAddOrEditBinding
import com.appsv.notesapp.notes.NotesViewModel
import kotlinx.coroutines.launch

/**
 * This Fragment is reusable for Add and Edit.
 */
class AddOrEditFragment : Fragment() {

    private lateinit var binding: FragmentAddOrEditBinding
    private var priority: Priority = Priority.LOW
    private val notesViewModel: NotesViewModel by viewModels()
//    private val isEditMode : LiveData<Boolean> = false
private val isEditModeLiveData = MutableLiveData<Boolean>(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddOrEditBinding.inflate(layoutInflater)

        binding.etEditTitle.requestFocus()


        setNotesDetailOnFields()
        onBackIconClick()
        onPriorityOvalsClick()
        onAddNoteButtonClicked()

        onDeleteIconClick()

        return binding.root
    }

    private fun onDeleteIconClick() {
        binding.ivDeleteIcon.setOnClickListener {
                val builder = AlertDialog.Builder(requireActivity())
                val alertDialog = builder.create()
                builder
                    .setTitle("Delete Note")
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton("Yes"){ _, _ ->
                        notesViewModel.deleteNoteById(arguments?.getInt("id")!!)
                        findNavController().navigate(R.id.action_addOrEditFragment_to_homeFragment)
                    }
                    .setNegativeButton("No"){ _, _ ->
                        alertDialog.dismiss()
                    }
                    .show()
                    .setCancelable(false)
            }

    }

    private fun setNotesDetailOnFields() {

        arguments?.let {

            val selectedNotes = it

            if (selectedNotes.getBoolean("editMode")){
                isEditModeLiveData.value = true
                editMode(selectedNotes)
            }

            else{
                // Add Mode
                binding.ivAddIcon.visibility = View.VISIBLE
                binding.tvAddText.visibility = View.VISIBLE
                binding.ivEditIcon.visibility = View.GONE
                binding.tvEditText.visibility = View.GONE
                binding.ivDeleteIcon.visibility = View.GONE
            }

        }
    }

    private fun editMode(selectedNotes: Bundle) {

        binding.ivAddIcon.visibility = View.GONE
        binding.tvAddText.visibility = View.GONE
        binding.ivEditIcon.visibility = View.VISIBLE
        binding.tvEditText.visibility = View.VISIBLE
        binding.ivDeleteIcon.visibility = View.VISIBLE

        val title = selectedNotes.getString("title")
        val subTitle = selectedNotes.getString("sub_title")
        val priority = selectedNotes.getInt("priority")
        val description = selectedNotes.getString("description")
        binding.etEditTitle.setText(title)
        binding.etEditSubtitle.setText(subTitle)
        binding.etEditDescription.setText(description)
        when (priority) {
            0 -> {
                binding.ivLowPriority.setImageResource(R.drawable.baseline_done_24)
                binding.ivMediumPriority.setImageResource(0)
                binding.ivHighPriority.setImageResource(0)
            }

            1 -> {
                binding.ivLowPriority.setImageResource(0)
                binding.ivMediumPriority.setImageResource(R.drawable.baseline_done_24)
                binding.ivHighPriority.setImageResource(0)
            }

            2 -> {
                binding.ivLowPriority.setImageResource(0)
                binding.ivMediumPriority.setImageResource(0)
                binding.ivHighPriority.setImageResource(R.drawable.baseline_done_24)
            }
        }
    }

    private fun onAddNoteButtonClicked() {
        binding.fabDone.setOnClickListener {
            lifecycleScope.launch {
                createNote()
            }
        }
    }

    private fun createNote() {

        val title = binding.etEditTitle.text.toString()
        val subTitle = binding.etEditSubtitle.text.toString()
        val description = binding.etEditDescription.text.toString()
        val dateAndTime = getCurrentLocalDateAndTimeInLong()
        val currentUserId = notesViewModel.getUserId()

        if (title != "") {


            var data = Notes(
                title = title,
                subTitle = subTitle,
                notes = description,
                date = dateAndTime,
                priority = priority,
                emailId = currentUserId!!
            )
            isEditModeLiveData.observe(viewLifecycleOwner){
                if(it == true){
                    val id = arguments?.getInt("id")
                    data = data.copy(id = id)
                    notesViewModel.updateNote(data)
                }
                else{
                    notesViewModel.saveOrUpdateNote(data)
                    Toasts.showSimpleToast("Notes Added Successfully", requireContext())
                }
            }


            findNavController().navigate(R.id.action_addOrEditFragment_to_homeFragment)
        } else {
            Toasts.showSimpleToast("Please enter title at least!", requireContext())
        }

    }

    private fun onPriorityOvalsClick() {
        // Low
        binding.ivLowPriority.setOnClickListener {
            priority = Priority.LOW
            binding.ivLowPriority.setImageResource(R.drawable.baseline_done_24)
            binding.ivMediumPriority.setImageResource(0)
            binding.ivHighPriority.setImageResource(0)
            Toasts.showSimpleToast("Priority : Low", requireContext())
        }

        // Medium
        binding.ivMediumPriority.setOnClickListener {
            priority = Priority.MEDIUM
            binding.ivMediumPriority.setImageResource(R.drawable.baseline_done_24)
            binding.ivLowPriority.setImageResource(0)
            binding.ivHighPriority.setImageResource(0)
            Toasts.showSimpleToast("Priority : Medium", requireContext())

        }

        // High
        binding.ivHighPriority.setOnClickListener {
            priority = Priority.HIGH
            binding.ivHighPriority.setImageResource(R.drawable.baseline_done_24)
            binding.ivMediumPriority.setImageResource(0)
            binding.ivLowPriority.setImageResource(0)
            Toasts.showSimpleToast("Priority : High", requireContext())
        }
    }


    private fun onBackIconClick() {
        binding.ivBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_addOrEditFragment_to_homeFragment)
        }
    }


}