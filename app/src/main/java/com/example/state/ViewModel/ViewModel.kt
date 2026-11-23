package com.example.state.ViewModel

import androidx.lifecycle.ViewModel
import com.example.state.Model.Gasto
import com.example.state.Model.PresupuestoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PresupuestoViewModel : ViewModel() {

    // 1. Estado privado mutable
    private val _uiState = MutableStateFlow(PresupuestoUiState())

    // 2. Estado público de solo lectura para la Vista
    val uiState: StateFlow<PresupuestoUiState> = _uiState.asStateFlow()

    // 3. Actualizar el ingreso mensual editable
    fun actualizarIngreso(nuevoIngreso: String) {
        _uiState.update { it.copy(ingresoText = nuevoIngreso) }
    }

    // 4. Agregar un gasto ya validado y empaquetado desde FormularioGasto
    fun agregarGasto(nuevoGasto: Gasto) {
        _uiState.update { estadoActual ->
            estadoActual.copy(
                listaGastos = estadoActual.listaGastos + nuevoGasto
            )
        }
    }

    // 5. Eliminar un gasto por su ID único
    fun eliminarGasto(idGasto: String) {
        _uiState.update { estadoActual ->
            estadoActual.copy(
                listaGastos = estadoActual.listaGastos.filterNot { it.id == idGasto }
            )
        }
    }
}