package com.example.financontrol.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.financontrol.data.local.db.AppDatabase
import com.example.financontrol.data.model.Transaction
import com.example.financontrol.data.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).transactionDao()
    private val repo = TransactionRepository(dao)

    val all: LiveData<List<Transaction>> = repo.all

    fun add(type: String, amount: Double, desc: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val tx = Transaction(
                type = type,
                amount = amount,
                description = desc.trim(),
                dateMillis = System.currentTimeMillis()
            )
            repo.add(tx)
        }
    }

    fun delete(tx: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.delete(tx)
        }
    }

    fun update(id: Int, type: String, amount: Double, desc: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val old = repo.getById(id) ?: return@launch

            // Mantiene el mismo id y la misma fecha
            val updated = old.copy(
                type = type,
                amount = amount,
                description = desc.trim()
            )
            repo.update(updated)
        }
    }

    suspend fun getById(id: Int): Transaction? {
        // Se usa desde LaunchedEffect en Compose
        return repo.getById(id)
    }
}
