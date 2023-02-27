package com.example.noteapp.data.repository

import androidx.lifecycle.LiveData
import com.example.noteapp.data.NoteDao
import com.example.noteapp.data.models.NoteData

class NoteRepository(
    private val noteDao : NoteDao
) {
    fun getAllData():LiveData<List<NoteData>> = noteDao.getAllData()

    suspend fun insertData(noteData: NoteData){
        noteDao.insertData(noteData)
    }
}