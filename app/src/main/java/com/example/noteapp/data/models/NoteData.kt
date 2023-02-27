package com.example.noteapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.noteapp.data.models.Priorities

@Entity(tableName = "note_table")
data class NoteData(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var priority: Priorities,
    var description: String
)
