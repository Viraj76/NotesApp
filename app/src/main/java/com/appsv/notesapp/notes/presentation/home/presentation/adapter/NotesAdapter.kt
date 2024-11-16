package com.appsv.notesapp.notes.presentation.home.presentation.adapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.appsv.notesapp.R
import com.appsv.notesapp.notes.domain.models.Notes
import com.appsv.notesapp.core.utils.enums.Priority
import com.appsv.notesapp.core.utils.getTimeAndDateInString
import com.appsv.notesapp.databinding.NotesItemBinding


class NotesAdapter:
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    class NotesViewHolder(val binding: NotesItemBinding) : RecyclerView.ViewHolder(binding.root)

   private val diffUtil = object : DiffUtil.ItemCallback<Notes>() {
        override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            NotesItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentNote=differ.currentList[position]
        holder.binding.cvTitle.text=currentNote.title
        holder.binding.cvSubtitle.text=currentNote.subTitle
        holder.binding.cvDate.text= getTimeAndDateInString(currentNote.date)
        if(currentNote.subTitle.isEmpty()){
            holder.binding.cvSubtitle.visibility = View.GONE
        }

        when(currentNote.priority){
            Priority.LOW->
                holder.binding.cvOval.setBackgroundResource(R.drawable.green_oval)
            Priority.MEDIUM->
                holder.binding.cvOval.setBackgroundResource(R.drawable.yellow_oval)
            Priority.HIGH->
                holder.binding.cvOval.setBackgroundResource(R.drawable.red_oval)
        }

        holder.binding.notesItemCard.setOnClickListener {
            val bundle = Bundle()
            bundle.apply {
                putInt("id", currentNote.id!!)
                putString("title",currentNote.title)
                putString("sub_title",currentNote.subTitle)
                putInt("priority",currentNote.priority.ordinal)
                putString("description",currentNote.notes)
                putBoolean("editMode",true)
            }
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_addOrEditFragment,bundle)
        }
    }
    override fun getItemCount(): Int = differ.currentList.size
}