package com.example.financontrol.utils

import org.junit.Assert.assertTrue
import org.junit.Test

class MoneyFormatterTest {

    @Test
    fun formatCLP_shouldShowPesoSymbol_thousandsWithDots_noDecimals() {
        val out = MoneyFormatter.formatCLP(1234567.0)

        // Debe tener símbolo $
        assertTrue(out.contains("$"))

        // Debe tener separador de miles con puntos
        assertTrue(out.contains("."))

        // No debe tener decimales
        assertTrue(!out.contains(","))
        assertTrue(!out.contains(".00"))
    }
}