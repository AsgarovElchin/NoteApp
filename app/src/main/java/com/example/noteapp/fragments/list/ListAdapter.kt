package com.example.noteapp.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import com.example.noteapp.data.models.NoteData
import com.example.noteapp.databinding.RowLayoutBinding

class ListAdapter :
    androidx.recyclerview.widget.ListAdapter<NoteData, ListViewHolder>(MainDiffUtils) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowLayoutBinding.inflate(inflater, parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(currentList[position])
        holder.binding.rowBackground.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentList[position])
            holder.binding.root.findNavController().navigate(action)
        }



    }

    object MainDiffUtils : DiffUtil.ItemCallback<NoteData>() {
        override fun areItemsTheSame(
            oldItem: NoteData,
            newItem: NoteData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: NoteData,
            newItem: NoteData
        ): Boolean {
            return oldItem.id == newItem.id
        }


    }

}