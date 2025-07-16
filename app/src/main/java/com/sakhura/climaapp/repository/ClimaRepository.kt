package com.sakhura.climaapp.repository

import com.sakhura.climaapp.api.ClimaApiService
import com.sakhura.climaapp.api.RetrofitClient
import com.sakhura.climaapp.model.ClimaResponse
import com.sakhura.climaapp.model.PronosticoResponse

class ClimaRepository {
    private val api = RetrofitClient.instance.create(ClimaApiService::class.java)
    private val apiKey = "TU_API_KEY"

    suspend fun obtenerClima(ciudad: String): ClimaResponse =
        api.obtenerClimaActual(ciudad, apiKey)

    suspend fun obtenerPronostico(ciudad: String): PronosticoResponse =
        api.obtenerPronostico(ciudad, apiKey)
}