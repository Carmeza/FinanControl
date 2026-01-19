package com.example.financontrol.utils

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object MoneyFormatter {

    private val clpFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply {
        currency = Currency.getInstance("CLP")
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }

    fun formatCLP(value: Double): String {
        // Redondea a entero
        return clpFormat.format(kotlin.math.round(value))
    }
}
