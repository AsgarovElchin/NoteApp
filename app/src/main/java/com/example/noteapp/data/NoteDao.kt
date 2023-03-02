package com.example.noteapp.data

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.noteapp.data.models.NoteData

@Dao
interface NoteDao {
    @Query("SELECT * FROM note_table ORDER BY id ASC")
    fun getAllData(): LiveData<List<NoteData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(noteData: NoteData)

    @Update
    suspend fun updateData(noteData: NoteData)

    @Delete
    suspend fun deleteItem(noteData: NoteData)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM note_table WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<NoteData>>

    @Query("SELECT * FROM note_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority():LiveData<List<NoteData>>

    @Query("SELECT * FROM note_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority():LiveData<List<NoteData>>

}