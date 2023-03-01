package com.example.noteapp.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.NoteDatabase
import com.example.noteapp.data.models.NoteData
import com.example.noteapp.data.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao = NoteDatabase.getDatabase(
        application
    ).noteDao()

    private val repository: NoteRepository

    val getAllData: LiveData<List<NoteData>>

    init {
        repository = NoteRepository((noteDao))
        getAllData = repository.getAllData()
    }

    fun insertData(noteData: NoteData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(noteData)
        }
    }

    fun updateData(noteData: NoteData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(noteData)
        }
    }

}