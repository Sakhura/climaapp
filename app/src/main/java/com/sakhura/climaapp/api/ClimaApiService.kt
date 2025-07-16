package com.sakhura.climaapp.api

import com.sakhura.climaapp.model.ClimaResponse
import com.sakhura.climaapp.model.PronosticoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ClimaApiService {

    @GET("weather")
    suspend fun obtenerClimaActual(
        @Query("q") ciudad: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): ClimaResponse

    @GET("forecast")
    suspend fun obtenerPronostico(
        @Query("q") ciudad: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es"
    ): PronosticoResponse
}
