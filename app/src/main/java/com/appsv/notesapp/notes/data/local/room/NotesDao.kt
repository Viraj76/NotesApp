package com.appsv.notesapp.notes.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.appsv.notesapp.notes.domain.models.Notes
import kotlinx.coroutines.flow.Flow



@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateNote(note: Notes)

    @Query("SELECT * FROM Notes WHERE emailId = :emailId ORDER BY date DESC")
    fun getNotesByEmailId(emailId: String): Flow<List<Notes>>

    @Query("DELETE FROM Notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Int)

    @Update
    suspend fun updateNoteById(note: Notes)
}

