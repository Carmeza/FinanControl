package com.example.financontrol.ui.compose.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.financontrol.data.model.Transaction
import com.example.financontrol.ui.compose.components.TransactionItem
import com.example.financontrol.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    vm: TransactionViewModel,
    onBack: () -> Unit,
    onEdit: (Int) -> Unit
) {
    val list by vm.all.observeAsState(emptyList())

    var txToDelete by remember { mutableStateOf<Transaction?>(null) }

    // Dialogo de confirmación
    if (txToDelete != null) {
        AlertDialog(
            onDismissRequest = { txToDelete = null },
            title = { Text("Eliminar transacción") },
            text = { Text("¿Seguro que quieres eliminar este registro?") },
            confirmButton = {
                TextButton(onClick = {
                    vm.delete(txToDelete!!)
                    txToDelete = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { txToDelete = null }) { Text("Cancelar") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Text("←") }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            items(list, key = { it.id }) { tx ->
                TransactionItem(
                    tx = tx,
                    onEdit = { onEdit(tx.id) },
                    onDelete = { txToDelete = tx }
                )
            }
        }
    }
}



