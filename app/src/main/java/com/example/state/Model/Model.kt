package com.example.state.Model

import kotlin.math.ceil

data class CuentaDivididaUiState(
    // 1. Datos ingresados por el usuario
    val montoCuenta: String = "",
    val propinaSeleccionada: Int = 10, // 10, 15, 20 o -1 para "Otro"
    val propinaPersonalizada: Float = 0f,
    val numeroPersonas: Int = 1,
    val redondearArriba: Boolean = true,
    val opcionesPropina: List<Int> = listOf(10, 15, 20, -1)
) {
    // 2. Validación y conversión segura del monto
    val montoSeguro: Double
        get() = montoCuenta.toDoubleOrNull() ?: 0.0

    val esMontoInvalido: Boolean
        get() = montoCuenta.isNotEmpty() && montoCuenta.toDoubleOrNull() == null

    // 3. Cálculos de propina y totales
    val esPropinaPersonalizada: Boolean
        get() = propinaSeleccionada == -1

    val porcentajeAplicar: Double
        get() = if (esPropinaPersonalizada) propinaPersonalizada.toDouble() else propinaSeleccionada.toDouble()

    val montoPropina: Double
        get() = montoSeguro * (porcentajeAplicar / 100.0)

    val totalConPropina: Double
        get() = montoSeguro + montoPropina

    // 4. Cálculo por persona y redondeo
    val montoPersonaCrudo: Double
        get() = if (numeroPersonas > 0) totalConPropina / numeroPersonas else 0.0

    val montoPersonaFinal: Double
        get() = if (redondearArriba) ceil(montoPersonaCrudo) else montoPersonaCrudo

    val sobranteRedondeo: Double
        get() = (montoPersonaFinal * numeroPersonas) - totalConPropina

    // 5. Reglas de límites del stepper (1 a 20)
    val puedeRestarPersona: Boolean
        get() = numeroPersonas > 1

    val puedeSumarPersona: Boolean
        get() = numeroPersonas < 20
}