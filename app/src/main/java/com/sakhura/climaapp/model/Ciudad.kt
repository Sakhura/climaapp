package com.sakhura.climaapp.model

data class Ciudad(
    val nombre: String,
    val fechaGuardado: Long = System.currentTimeMillis()
)
