package com.example.state.ViewModel

import androidx.lifecycle.ViewModel
import com.example.state.Model.LecturaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LecturaViewModel : ViewModel() {

    // 1. Estado privado mutable (solo el ViewModel puede modificarlo)
    private val _uiState = MutableStateFlow(LecturaUiState())

    // 2. Estado público de solo lectura (la Vista lo observa)
    val uiState: StateFlow<LecturaUiState> = _uiState.asStateFlow()

    // 3. Funciones para actualizar cada control de la pantalla
    fun actualizarTamanoLetra(nuevoTamano: Float) {
        _uiState.update { estadoActual ->
            estadoActual.copy(fontSize = nuevoTamano)
        }
    }

    fun actualizarInterlineado(nuevoInterlineado: Float) {
        _uiState.update { estadoActual ->
            estadoActual.copy(lineSpacing = nuevoInterlineado)
        }
    }

    fun cambiarModoOscuro(activado: Boolean) {
        _uiState.update { estadoActual ->
            estadoActual.copy(isDarkMode = activado)
        }
    }

    fun cambiarFuente(usarSerif: Boolean) {
        _uiState.update { estadoActual ->
            estadoActual.copy(isSerif = usarSerif)
        }
    }

    fun restablecerValores() {
        // Vuelve a los valores por defecto definidos en LecturaUiState (18f, 1.3f, false, false)
        _uiState.value = LecturaUiState()
    }
}