package com.example.contacts

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface Dao {
    @Insert
    suspend fun insert(contact: Contanct)

    @Query("SELECT*FROM contact_table")
    suspend fun getAllData(): List<Contanct>

    @Delete
    suspend fun delete(contact: Contanct)

    @Update
    suspend fun update(contact: Contanct)
}