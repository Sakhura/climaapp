package com.sakhura.climaapp.model


data class PronosticoResponse(
    val list: List<PronosticoDia>
)

data class PronosticoDia(
    val dt_txt: String,
    val main: Main,
    val weather: List<Weather>
)

