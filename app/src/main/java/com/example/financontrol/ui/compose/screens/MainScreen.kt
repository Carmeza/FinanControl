package com.example.financontrol.ui.compose.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financontrol.utils.MoneyFormatter
import com.example.financontrol.viewmodel.ExchangeRateViewModel
import com.example.financontrol.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    vm: TransactionViewModel,
    onGoHistory: () -> Unit,
    onGoAdd: () -> Unit
) {
    val list by vm.all.observeAsState(emptyList())

    val totalIncome = list.filter { it.type.equals("INGRESO", true) }.sumOf { it.amount }
    val totalExpense = list.filter { it.type.equals("GASTO", true) }.sumOf { it.amount }
    val balance = totalIncome - totalExpense

    val rateVm: ExchangeRateViewModel = viewModel()
    LaunchedEffect(Unit) { rateVm.loadUsdToClp() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("FinanControl") }) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Resumen
            Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Resumen")
                    Text("Ingresos: ${MoneyFormatter.formatCLP(totalIncome)}")
                    Text("Gastos: ${MoneyFormatter.formatCLP(totalExpense)}")
                    Text("Balance: ${MoneyFormatter.formatCLP(balance)}")
                }
            }

            // API
            Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Tipo de cambio")
                    val st = rateVm.uiState

                    when {
                        st.loading -> Text("Cargando USD a CLP…")
                        st.usdToClp != null -> {
                            Text("USD = CLP: ${st.usdToClp}")
                            Text("Última actualización: ${st.lastUpdateUtc ?: "-"}")

                        }
                        else -> Text("Error: ${st.error ?: "No se pudo obtener la tasa"}")
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { rateVm.loadUsdToClp() }) {
                        Text("Actualizar tasa")
                    }
                }
            }

            Button(onClick = onGoAdd) { Text("Agregar ingreso/gasto") }
            Button(onClick = onGoHistory) { Text("Ver historial") }
        }
    }
}
