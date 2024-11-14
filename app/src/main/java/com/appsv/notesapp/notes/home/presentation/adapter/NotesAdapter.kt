package com.appsv.notesapp.notes.home.presentation.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgumentBuilder
import androidx.navigation.Navigation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.appsv.notesapp.R
import com.appsv.notesapp.core.domain.Notes
import com.appsv.notesapp.core.util.enums.Priority
import com.appsv.notesapp.core.util.getTimeAndDateInString
import com.appsv.notesapp.databinding.NotesItemBinding


class NotesAdapter:
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    class NotesViewHolder(val binding: NotesItemBinding) : RecyclerView.ViewHolder(binding.root)

    val diffutil = object : DiffUtil.ItemCallback<Notes>() {
        override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffutil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            NotesItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }
    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val data=differ.currentList[position]
        holder.binding.cvTitle.text=data.title
        holder.binding.cvSubtitle.text=data.subTitle
        holder.binding.cvDate.text= getTimeAndDateInString(data.date)
        if(data.subTitle.isEmpty()){
            holder.binding.cvSubtitle.visibility = View.GONE
        }

        when(data.priority){
            Priority.LOW->
                holder.binding.cvOval.setBackgroundResource(R.drawable.green_oval)
            Priority.MEDIUM->
                holder.binding.cvOval.setBackgroundResource(R.drawable.yellow_oval)
            Priority.HIGH->
                holder.binding.cvOval.setBackgroundResource(R.drawable.red_oval)
        }

//        holder.binding.root.setOnClickListener {
//            val action = HomeFragmentDirections.actionHomeFragment2ToEditFragment(data)
//            Navigation.findNavController(it).navigate(action)
//        }
    }
    override fun getItemCount(): Int = differ.currentList.size
}