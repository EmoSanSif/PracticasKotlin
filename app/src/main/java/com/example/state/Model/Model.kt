package com.example.state.Model

import java.util.UUID

// 1. Modelo de cada tarea (inmutable para que funcione bien con StateFlow)
data class Tarea(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val prioridad: String,
    val completada: Boolean = false
)

// 2. Estado completo de la pantalla
data class TareasUiState(
    val textoNuevaTarea: String = "",
    val prioridadSeleccionada: String = "Alta",
    val filtroActivo: String = "Todas",
    val listaTareas: List<Tarea> = listOf(
        Tarea(nombre = "Entregar proyecto POO", prioridad = "Alta", completada = false),
        Tarea(nombre = "Leer capítulo 4", prioridad = "Baja", completada = true)
    ),
    val mostrarDialogoBorrar: Boolean = false,
    val opcionesPrioridad: List<String> = listOf("Alta", "Media", "Baja"),
    val opcionesFiltro: List<String> = listOf("Todas", "Pendientes", "Hechas")
) {
    // 3. Propiedades derivadas (Cálculos y filtros puros)
    val tareasFiltradas: List<Tarea>
        get() = when (filtroActivo) {
            "Pendientes" -> listaTareas.filter { !it.completada }
            "Hechas" -> listaTareas.filter { it.completada }
            else -> listaTareas
        }

    val cantidadPendientes: Int
        get() = listaTareas.count { !it.completada }

    val cantidadCompletadas: Int
        get() = listaTareas.count { it.completada }

    val totalTareas: Int
        get() = listaTareas.size

    val puedeAgregarTarea: Boolean
        get() = textoNuevaTarea.isNotBlank()

    val hayCompletadasParaBorrar: Boolean
        get() = cantidadCompletadas > 0
}