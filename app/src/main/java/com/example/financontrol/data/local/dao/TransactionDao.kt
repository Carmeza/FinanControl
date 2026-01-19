package com.example.financontrol.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.financontrol.data.model.Transaction

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(tx: Transaction)

    @Query("SELECT * FROM transactions ORDER BY dateMillis DESC")
    fun getAll(): LiveData<List<Transaction>>
}
