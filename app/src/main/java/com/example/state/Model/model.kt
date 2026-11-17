package com.example.state.Model


data class BibliotecaUiState(
    val personasActuales: Int = 0,
    val visitasDelDia: Int = 0,
    val capacidadMaxima: Int = 50
) {
    // Propiedades calculadas (lógica pura de datos, sin UI)
    val porcentajeOcupacion: Float
        get() = if (capacidadMaxima > 0) personasActuales.toFloat() / capacidadMaxima else 0f

    val porcentajeTexto: Int
        get() = (porcentajeOcupacion * 100).toInt()

    val puedeEntrar: Boolean
        get() = personasActuales < capacidadMaxima

    val puedeSalir: Boolean
        get() = personasActuales > 0
}