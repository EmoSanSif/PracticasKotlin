package com.example.state.ViewModel

import com.example.state.Model.Tarea
import com.example.state.Model.TareasUiState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TareasViewModel : ViewModel() {

    // 1. Estado privado mutable
    private val _uiState = MutableStateFlow(TareasUiState())

    // 2. Estado público de solo lectura para la Vista
    val uiState: StateFlow<TareasUiState> = _uiState.asStateFlow()

    // 3. Eventos de entrada y selección
    fun actualizarTextoTarea(nuevoTexto: String) {
        _uiState.update { it.copy(textoNuevaTarea = nuevoTexto) }
    }

    fun seleccionarPrioridad(nuevaPrioridad: String) {
        _uiState.update { it.copy(prioridadSeleccionada = nuevaPrioridad) }
    }

    fun cambiarFiltro(nuevoFiltro: String) {
        _uiState.update { it.copy(filtroActivo = nuevoFiltro) }
    }

    // 4. Acciones CRUD sobre la lista de tareas
    fun agregarTarea() {
        _uiState.update { estadoActual ->
            if (estadoActual.puedeAgregarTarea) {
                val nueva = Tarea(
                    nombre = estadoActual.textoNuevaTarea.trim(),
                    prioridad = estadoActual.prioridadSeleccionada
                )
                estadoActual.copy(
                    listaTareas = estadoActual.listaTareas + nueva,
                    textoNuevaTarea = "" // Limpia el campo tras agregar
                )
            } else {
                estadoActual
            }
        }
    }

    fun alternarCompletada(idTarea: String) {
        _uiState.update { estadoActual ->
            val listaActualizada = estadoActual.listaTareas.map { tarea ->
                if (tarea.id == idTarea) {
                    tarea.copy(completada = !tarea.completada)
                } else {
                    tarea
                }
            }
            estadoActual.copy(listaTareas = listaActualizada)
        }
    }

    fun eliminarTarea(idTarea: String) {
        _uiState.update { estadoActual ->
            estadoActual.copy(
                listaTareas = estadoActual.listaTareas.filterNot { it.id == idTarea }
            )
        }
    }

    // 5. Control del AlertDialog de confirmación
    fun abrirDialogoBorrar() {
        _uiState.update { it.copy(mostrarDialogoBorrar = true) }
    }

    fun cerrarDialogoBorrar() {
        _uiState.update { it.copy(mostrarDialogoBorrar = false) }
    }

    fun confirmarBorrarCompletadas() {
        _uiState.update { estadoActual ->
            estadoActual.copy(
                listaTareas = estadoActual.listaTareas.filterNot { it.completada },
                mostrarDialogoBorrar = false
            )
        }
    }
}