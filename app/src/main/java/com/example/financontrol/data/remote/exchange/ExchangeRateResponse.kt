package com.example.financontrol.data.remote.exchange

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    val result: String? = null,
    val provider: String? = null,
    val documentation: String? = null,
    val terms_of_use: String? = null,
    val time_last_update_utc: String? = null,
    val time_next_update_utc: String? = null,
    val base_code: String? = null,
    val rates: Map<String, Double> = emptyMap(),

    @SerializedName("error-type")
    val errorType: String? = null
)