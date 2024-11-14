package com.appsv.notesapp.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.appsv.notesapp.core.domain.Notes
import com.appsv.notesapp.core.domain.models.LoggedInUserDetail
import com.appsv.notesapp.core.util.enums.Priority
import kotlinx.coroutines.flow.Flow

@Dao
interface LoggedInUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(authResult: LoggedInUserDetail)

    @Query("SELECT * FROM LoggedInUserDetail WHERE id = :userId LIMIT 1")
     fun getUserById(userId: String): Flow<LoggedInUserDetail?>

    @Query("DELETE FROM LoggedInUserDetail WHERE id = :userId")
    suspend fun deleteUserById(userId: String)

    @Query("DELETE FROM LoggedInUserDetail")
    suspend fun deleteAllUsers()

}


@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateNote(note: Notes)

    @Query("SELECT * FROM Notes WHERE emailId = :emailId")
    fun getNotesByEmailId(emailId: String): Flow<List<Notes>>

    @Query("DELETE FROM Notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Int)

    @Update
    suspend fun updateNoteById(note: Notes)
}

