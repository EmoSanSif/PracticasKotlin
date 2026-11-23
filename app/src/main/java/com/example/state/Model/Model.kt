package com.example.state.Model

import java.util.UUID

// 1. Modelo de cada gasto (con ID único para eliminarlo sin ambigüedad)
data class Gasto(
    val id: String = UUID.randomUUID().toString(),
    val concepto: String,
    val monto: Double,
    val categoria: String
)

// 2. Estado completo del presupuesto mensual
data class PresupuestoUiState(
    val ingresoText: String = "4500",
    val listaGastos: List<Gasto> = emptyList(),
    val categoriasDisponibles: List<String> = listOf(
        "Comida", "Transporte", "Renta", "Ocio", "Otros"
    )
) {
    // 3. Cálculos derivados (Lógica financiera pura)
    val ingresoMensual: Double
        get() = ingresoText.toDoubleOrNull() ?: 0.0

    val totalGastado: Double
        get() = listaGastos.sumOf { it.monto }

    val saldoDisponible: Double
        get() = ingresoMensual - totalGastado

    val porcentajeUsado: Double
        get() = if (ingresoMensual > 0) (totalGastado / ingresoMensual) else 0.0

    val porcentajeTexto: Int
        get() = (porcentajeUsado * 100).toInt()

    // Búsqueda de la categoría con mayor gasto
    val categoriaMayorGasto: Map.Entry<String, Double>?
        get() = listaGastos
            .groupBy { it.categoria }
            .mapValues { entry -> entry.value.sumOf { it.monto } }
            .maxByOrNull { it.value }

    // 4. Banderas de alertas y estados visuales derivados
    val mostrarAlerta80: Boolean
        get() = porcentajeTexto >= 80

    val presupuestoExcedido: Boolean
        get() = porcentajeTexto >= 100

    val montoExcedido: Double
        get() = if (presupuestoExcedido) totalGastado - ingresoMensual else 0.0
}