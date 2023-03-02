package com.example.noteapp.data.repository

import android.provider.ContactsContract.CommonDataKinds.Note
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
    suspend fun updateData(noteData: NoteData){
        noteDao.updateData(noteData)
    }

    suspend fun deleteItem(noteData: NoteData){
        noteDao.deleteItem(noteData)
    }

    suspend fun deleteAll(){
        noteDao.deleteAll()
    }

    fun searchDatabase(searchQuery:String):LiveData<List<NoteData>>{
        return noteDao.searchDatabase(searchQuery)}

}