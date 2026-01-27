package com.example.financontrol.ui.compose.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.financontrol.viewmodel.TransactionViewModel
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    vm: TransactionViewModel,
    onBack: () -> Unit,
    editId: Int = -1
) {
    var type by rememberSaveable { mutableStateOf("GASTO") }
    var amountText by rememberSaveable { mutableStateOf("") }
    var desc by rememberSaveable { mutableStateOf("") }

    var amountError by rememberSaveable { mutableStateOf<String?>(null) }
    var descError by rememberSaveable { mutableStateOf<String?>(null) }

    // ---------- Recurso nativo: Cámara ----------
    var receiptBitmap by rememberSaveable { mutableStateOf<Bitmap?>(null) }
    var cameraStatus by rememberSaveable { mutableStateOf("Boleta: (sin foto)") }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bmp ->
        receiptBitmap = bmp
        cameraStatus = if (bmp != null) "Boleta: foto capturada ✅" else "Boleta: no se capturó"
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) cameraLauncher.launch(null)
        else cameraStatus = "Boleta: permiso de cámara denegado"
    }

    // ---------- Recurso nativo: GPS ----------
    val context = LocalContext.current
    var locationText by rememberSaveable { mutableStateOf("Ubicación: (no capturada)") }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val fine = result[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarse = result[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fine || coarse) {
            fetchLastLocation(context) { txt -> locationText = txt }
        } else {
            locationText = "Ubicación: permiso denegado"
        }
    }

    LaunchedEffect(editId) {
        if (editId != -1) {
            val tx = vm.getById(editId)
            if (tx != null) {
                type = tx.type
                amountText = tx.amount.toLong().toString()
                desc = tx.description
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (editId == -1) "Agregar transacción" else "Editar transacción") },
                navigationIcon = { IconButton(onClick = onBack) { Text("←") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Tipo")

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { type = "INGRESO" },
                    modifier = Modifier.weight(1f)
                ) { Text("INGRESO") }

                Button(
                    onClick = { type = "GASTO" },
                    modifier = Modifier.weight(1f)
                ) { Text("GASTO") }
            }

            OutlinedTextField(
                value = amountText,
                onValueChange = { newValue ->
                    amountText = newValue.filter { it.isDigit() } // sin decimales
                    amountError = null
                },
                label = { Text("Monto (sin decimales)") },
                modifier = Modifier.fillMaxWidth(),
                isError = amountError != null,
                supportingText = { if (amountError != null) Text(amountError!!) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            OutlinedTextField(
                value = desc,
                onValueChange = {
                    desc = it
                    descError = null
                },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                isError = descError != null,
                supportingText = { if (descError != null) Text(descError!!) }
            )

            // ---------- Recursos nativos ----------

            // Cámara
            Button(
                onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tomar foto de boleta (Cámara)")
            }
            Text(cameraStatus)

            if (receiptBitmap != null) {
                Image(
                    bitmap = receiptBitmap!!.asImageBitmap(),
                    contentDescription = "Boleta capturada",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // GPS
            Button(
                onClick = {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Obtener ubicación (GPS)")
            }
            Text(locationText)

            Spacer(modifier = Modifier.height(8.dp))

            // Guardar / Actualizar
            Button(
                onClick = {
                    // Validaciones
                    if (amountText.isBlank()) {
                        amountError = "Ingresa un monto"
                        return@Button
                    }
                    val amount = amountText.toLongOrNull()?.toDouble()
                    if (amount == null || amount <= 0) {
                        amountError = "Monto inválido"
                        return@Button
                    }
                    if (desc.trim().isEmpty()) {
                        descError = "Ingresa una descripción"
                        return@Button
                    }

                    val extras = buildString {
                        if (locationText.startsWith("Ubicación:") && !locationText.contains("no capturada")) {
                            append(" | ")
                            append(locationText)
                        }
                        if (receiptBitmap != null) {
                            append(" | Boleta: Sí")
                        }
                    }
                    val finalDesc = (desc.trim() + extras).trim()

                    if (editId == -1) vm.add(type, amount, finalDesc)
                    else vm.update(editId, type, amount, finalDesc)

                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (editId == -1) "Guardar" else "Actualizar")
            }
        }
    }
}

@SuppressLint("MissingPermission")
private fun fetchLastLocation(context: Context, onResult: (String) -> Unit) {
    val client = LocationServices.getFusedLocationProviderClient(context)
    client.lastLocation
        .addOnSuccessListener { loc ->
            if (loc != null) {
                onResult("Ubicación: ${loc.latitude}, ${loc.longitude}")
            } else {
                onResult("Ubicación: no disponible (activa GPS/Ubicación)")
            }
        }
        .addOnFailureListener {
            onResult("Ubicación: error al obtener")
        }
}