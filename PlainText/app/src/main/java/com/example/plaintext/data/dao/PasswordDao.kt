package com.example.plaintext.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.plaintext.data.model.Password
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {
    @Query("SELECT * FROM passwords ORDER BY name ASC")
    fun getAll(): Flow<List<Password>>

    @Query("SELECT * FROM passwords WHERE id = :id")
    fun get(id: Int): Flow<Password>

    @Insert
    suspend fun insert(password: Password): Long

    @Update
    suspend fun update(password: Password)

    @Query("SELECT COUNT(*) FROM passwords")
    fun getRowCount(): Flow<Int>
}