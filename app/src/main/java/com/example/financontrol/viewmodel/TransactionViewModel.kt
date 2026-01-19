package com.example.financontrol.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.financontrol.data.local.db.AppDatabase
import com.example.financontrol.data.model.Transaction
import com.example.financontrol.data.repository.TransactionRepository
import kotlinx.coroutines.launch

class TransactionViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.getInstance(app).transactionDao()
    private val repo = TransactionRepository(dao)

    val all: LiveData<List<Transaction>> = repo.all

    fun add(type: String, amount: Double, desc: String) {
        viewModelScope.launch {
            repo.add(
                Transaction(
                    type = type,
                    amount = amount,
                    description = desc
                )
            )
        }
    }
}
