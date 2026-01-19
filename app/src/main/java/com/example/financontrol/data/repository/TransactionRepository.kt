package com.example.financontrol.data.repository

import androidx.lifecycle.LiveData
import com.example.financontrol.data.local.dao.TransactionDao
import com.example.financontrol.data.model.Transaction

class TransactionRepository(private val dao: TransactionDao) {

    val all: LiveData<List<Transaction>> = dao.getAll()

    suspend fun add(tx: Transaction) {
        dao.insert(tx)
    }
}
