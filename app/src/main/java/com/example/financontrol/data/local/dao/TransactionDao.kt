package com.example.financontrol.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.financontrol.data.model.Transaction

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(tx: Transaction)

    @Update
    suspend fun update(tx: Transaction)

    @Delete
    suspend fun delete(tx: Transaction)

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Transaction?

    @Query("SELECT * FROM transactions ORDER BY dateMillis DESC")
    fun getAll(): LiveData<List<Transaction>>
}