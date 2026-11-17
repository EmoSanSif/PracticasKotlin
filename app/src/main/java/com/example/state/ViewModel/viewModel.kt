package com.example.state.ViewModel

import androidx.lifecycle.ViewModel
import com.example.state.Model.BibliotecaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BibliotecaViewModel : ViewModel() {

    // 1. Estado privado (solo el ViewModel puede modificarlo)
    private val _uiState = MutableStateFlow(BibliotecaUiState())

    // 2. Estado público de solo lectura (la Vista lo observa)
    val uiState: StateFlow<BibliotecaUiState> = _uiState.asStateFlow()

    // 3. Acciones que antes estaban en los onClick de los botones
    fun registrarEntrada() {
        _uiState.update { estadoActual ->
            if (estadoActual.puedeEntrar) {
                estadoActual.copy(
                    personasActuales = estadoActual.personasActuales + 1,
                    visitasDelDia = estadoActual.visitasDelDia + 1
                )
            } else {
                estadoActual
            }
        }
    }

    fun registrarSalida() {
        _uiState.update { estadoActual ->
            if (estadoActual.puedeSalir) {
                estadoActual.copy(
                    personasActuales = estadoActual.personasActuales - 1
                )
            } else {
                estadoActual
            }
        }
    }

    fun cerrarDia() {
        _uiState.value = BibliotecaUiState() // Reinicia todo a los valores por defecto (0)
    }
}