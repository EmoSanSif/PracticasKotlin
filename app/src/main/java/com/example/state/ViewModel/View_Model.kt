package com.example.state.ViewModel

import androidx.lifecycle.ViewModel
import com.example.state.Model.CarritoUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CarritoViewModel : ViewModel() {

    // 1. Estado privado mutable
    private val _uiState = MutableStateFlow(CarritoUiState())

    // 2. Estado público de solo lectura para la Vista
    val uiState: StateFlow<CarritoUiState> = _uiState.asStateFlow()

    // 3. Control de cantidades por producto (0 a 10)
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
                        remove(idProducto) // Si llega a 0, lo quitamos del mapa para mantenerlo limpio
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

    // 4. Acciones de los botones inferiores
    fun vaciarCarrito() {
        _uiState.update { it.copy(cantidades = emptyMap()) }
    }

    fun confirmarPedido() {
        // Reinicia el carrito tras confirmar el pedido (igual que en tu código original)
        _uiState.update { it.copy(cantidades = emptyMap()) }
    }
}