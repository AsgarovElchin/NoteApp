package com.example.noteapp.fragments.list

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.data.models.NoteData
import com.example.noteapp.data.models.Priorities
import com.example.noteapp.databinding.RowLayoutBinding

class ListViewHolder(val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(noteData: NoteData) {
        binding.descriptionTxt.text = noteData.description
        binding.titleTxt.text = noteData.title
        when (noteData.priority) {
            Priorities.HIGH -> binding.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(
                    itemView.context, R.color.red
                )
            )

            Priorities.MEDIUM -> binding.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(
                    itemView.context, R.color.yellow
                )
            )

            Priorities.LOW -> binding.priorityIndicator.setCardBackgroundColor(
                ContextCompat.getColor(
                    itemView.context, R.color.green
                )
            )
        }

    }
}