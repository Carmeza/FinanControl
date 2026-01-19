package com.example.financontrol.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,          // "GASTO" o "INGRESO"
    val amount: Double,
    val description: String,
    val dateMillis: Long = System.currentTimeMillis()
)
