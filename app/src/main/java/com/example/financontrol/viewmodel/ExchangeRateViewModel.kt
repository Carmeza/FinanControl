package com.example.financontrol.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financontrol.data.remote.exchange.Network
import kotlinx.coroutines.launch

data class ExchangeUiState(
    val loading: Boolean = false,
    val usdToClp: Double? = null,
    val lastUpdateUtc: String? = null,
    val nextUpdateUtc: String? = null,
    val error: String? = null
)

class ExchangeRateViewModel : ViewModel() {

    var uiState by mutableStateOf(ExchangeUiState(loading = true))
        private set

    fun loadUsdToClp() {
        viewModelScope.launch {
            uiState = ExchangeUiState(loading = true)

            runCatching {
                Network.api.latest("USD")
            }.onSuccess { resp ->
                if (resp.result != "success") {
                    uiState = ExchangeUiState(
                        loading = false,
                        error = resp.errorType ?: "Error en API"
                    )
                    return@onSuccess
                }

                val clp = resp.rates["CLP"]
                uiState = ExchangeUiState(
                    loading = false,
                    usdToClp = clp,
                    lastUpdateUtc = resp.time_last_update_utc,
                    nextUpdateUtc = resp.time_next_update_utc
                )
            }.onFailure { e ->
                uiState = ExchangeUiState(
                    loading = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }
}