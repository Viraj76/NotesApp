package com.appsv.notesapp.core.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appsv.notesapp.core.domain.models.LoggedInUserDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface LoggedInUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(authResult: LoggedInUserDetail)

    @Query("SELECT * FROM LoggedInUserDetail WHERE id = :userId LIMIT 1")
     fun getUserById(userId: String): LoggedInUserDetail

    @Query("SELECT * FROM LoggedInUserDetail")
    fun getAllUsers(): Flow<List<LoggedInUserDetail?>>

    @Query("DELETE FROM LoggedInUserDetail WHERE id = :userId")
    suspend fun deleteUserById(userId: String)

    @Query("DELETE FROM LoggedInUserDetail")
    suspend fun deleteAllUsers()

}


