package com.sakhura.climaapp.repository

import com.sakhura.climaapp.api.ClimaApiService
import com.sakhura.climaapp.api.RetrofitClient
import com.sakhura.climaapp.model.ClimaResponse
import com.sakhura.climaapp.model.PronosticoResponse

class ClimaRepository {
    private val api = RetrofitClient.instance.create(ClimaApiService::class.java)

    // PASO IMPORTANTE:
    // 1. Ve a https://openweathermap.org/api
    // 2. Crea una cuenta gratuita
    // 3. Obtén tu API key
    // 4. Reemplaza "TU_API_KEY_AQUI" con tu API key real
    private val apiKey = "TU_API_KEY_AQUI"

    suspend fun obtenerClima(ciudad: String): ClimaResponse {
        return try {
            if (apiKey == "TU_API_KEY_AQUI") {
                throw Exception("Por favor configura tu API key de OpenWeatherMap")
            }
            api.obtenerClimaActual(ciudad, apiKey)
        } catch (e: Exception) {
            throw Exception("Error al obtener el clima: ${e.message}")
        }
    }

    suspend fun obtenerPronostico(ciudad: String): PronosticoResponse {
        return try {
            if (apiKey == "TU_API_KEY_AQUI") {
                throw Exception("Por favor configura tu API key de OpenWeatherMap")
            }
            api.obtenerPronostico(ciudad, apiKey)
        } catch (e: Exception) {
            throw Exception("Error al obtener el pronóstico: ${e.message}")
        }
    }
}