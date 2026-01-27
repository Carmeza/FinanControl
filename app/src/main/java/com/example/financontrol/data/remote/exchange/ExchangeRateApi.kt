package com.example.financontrol.data.remote.exchange

import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRateApi {
    @GET("v6/latest/{base}")
    suspend fun latest(@Path("base") base: String): ExchangeRateResponse
}