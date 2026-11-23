package com.example.state.ViewModel

import androidx.lifecycle.ViewModel
import com.example.state.Model.InscripcionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InscripcionViewModel : ViewModel() {

    // 1. Estado privado mutable
    private val _uiState = MutableStateFlow(InscripcionUiState())

    // 2. Estado público de solo lectura para la Vista
    val uiState: StateFlow<InscripcionUiState> = _uiState.asStateFlow()

    // 3. Eventos de actualización de campos
    fun actualizarNombre(nuevoNombre: String) {
        _uiState.update { it.copy(nombre = nuevoNombre) }
    }

    fun actualizarCorreo(nuevoCorreo: String) {
        _uiState.update { it.copy(correo = nuevoCorreo) }
    }

    fun actualizarEdad(nuevaEdad: String) {
        _uiState.update { it.copy(edad = nuevaEdad) }
    }

    fun seleccionarTaller(taller: String) {
        _uiState.update { it.copy(tallerSeleccionado = taller) }
    }

    fun cambiarPrivacidad(aceptada: Boolean) {
        _uiState.update { it.copy(privacidadAceptada = aceptada) }
    }

    // 4. Acciones de flujo (Botones)
    fun intentarInscripcion() {
        _uiState.update { estadoActual ->
            // Activamos intentoEnvio para que se muestren los errores si hay campos inválidos
            val puedeAvanzar = estadoActual.formularioValido
            estadoActual.copy(
                intentoEnvio = true,
                pantallaConfirmacion = puedeAvanzar
            )
        }
    }

    fun nuevaInscripcion() {
        // Reinicia absolutamente todo a sus valores iniciales
        _uiState.value = InscripcionUiState()
    }
}