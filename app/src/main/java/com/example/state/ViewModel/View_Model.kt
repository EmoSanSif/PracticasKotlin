package com.example.state.ViewModel

import androidx.lifecycle.ViewModel
import com.example.state.Model.CuentaDivididaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CuentaDivididaViewModel : ViewModel() {

    // 1. Estado privado mutable
    private val _uiState = MutableStateFlow(CuentaDivididaUiState())

    // 2. Estado público de solo lectura para la Vista
    val uiState: StateFlow<CuentaDivididaUiState> = _uiState.asStateFlow()

    // 3. Acciones de los controles de la interfaz
    fun actualizarMonto(nuevoMonto: String) {
        _uiState.update { it.copy(montoCuenta = nuevoMonto) }
    }

    fun seleccionarPropina(valorPropina: Int) {
        _uiState.update { it.copy(propinaSeleccionada = valorPropina) }
    }

    fun actualizarPropinaPersonalizada(nuevoPorcentaje: Float) {
        _uiState.update { it.copy(propinaPersonalizada = nuevoPorcentaje) }
    }

    fun sumarPersona() {
        _uiState.update { estadoActual ->
            if (estadoActual.puedeSumarPersona) {
                estadoActual.copy(numeroPersonas = estadoActual.numeroPersonas + 1)
            } else {
                estadoActual
            }
        }
    }

    fun restarPersona() {
        _uiState.update { estadoActual ->
            if (estadoActual.puedeRestarPersona) {
                estadoActual.copy(numeroPersonas = estadoActual.numeroPersonas - 1)
            } else {
                estadoActual
            }
        }
    }

    fun cambiarRedondeo(activar: Boolean) {
        _uiState.update { it.copy(redondearArriba = activar) }
    }
}