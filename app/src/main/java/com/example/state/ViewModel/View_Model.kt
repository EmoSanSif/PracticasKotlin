package com.example.state.ViewModel

import androidx.lifecycle.ViewModel
import com.example.state.Model.CarritoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CarritoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CarritoUiState())

    val uiState: StateFlow<CarritoUiState> = _uiState.asStateFlow()

    fun incrementarCantidad(idProducto: Int) {
        _uiState.update { estadoActual ->
            val cantidadActual = estadoActual.cantidadDe(idProducto)
            if (cantidadActual < 10) {
                val nuevasCantidades = estadoActual.cantidades.toMutableMap().apply {
                    put(idProducto, cantidadActual + 1)
                }
                estadoActual.copy(cantidades = nuevasCantidades)
            } else {
                estadoActual
            }
        }
    }

    fun decrementarCantidad(idProducto: Int) {
        _uiState.update { estadoActual ->
            val cantidadActual = estadoActual.cantidadDe(idProducto)
            if (cantidadActual > 0) {
                val nuevasCantidades = estadoActual.cantidades.toMutableMap().apply {
                    if (cantidadActual - 1 == 0) {
                        remove(idProducto)
                    } else {
                        put(idProducto, cantidadActual - 1)
                    }
                }
                estadoActual.copy(cantidades = nuevasCantidades)
            } else {
                estadoActual
            }
        }
    }

    fun vaciarCarrito() {
        _uiState.update { it.copy(cantidades = emptyMap()) }
    }

    fun confirmarPedido() {
        _uiState.update { it.copy(cantidades = emptyMap()) }
    }
}