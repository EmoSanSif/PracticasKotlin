package com.example.state.ViewModel

import com.example.state.Model.ResenaUiState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ResenaViewModel : ViewModel() {

    // 1. Estado privado mutable
    private val _uiState = MutableStateFlow(ResenaUiState())

    // 2. Estado público de solo lectura para la Vista
    val uiState: StateFlow<ResenaUiState> = _uiState.asStateFlow()

    // 3. Eventos de calificación por categoría
    fun calificarComida(estrellas: Int) {
        _uiState.update { it.copy(califComida = estrellas) }
    }

    fun calificarServicio(estrellas: Int) {
        _uiState.update { it.copy(califServicio = estrellas) }
    }

    fun calificarAmbiente(estrellas: Int) {
        _uiState.update { it.copy(califAmbiente = estrellas) }
    }

    // 4. Actualización de comentario respetando el límite máximo (200 caracteres)
    fun actualizarComentario(nuevoTexto: String) {
        _uiState.update { estadoActual ->
            if (nuevoTexto.length <= estadoActual.maxCaracteres) {
                estadoActual.copy(comentario = nuevoTexto)
            } else {
                estadoActual
            }
        }
    }

    // 5. Selección múltiple de etiquetas (Set)
    fun alternarEtiqueta(etiqueta: String) {
        _uiState.update { estadoActual ->
            val nuevasEtiquetas = if (estadoActual.etiquetasSeleccionadas.contains(etiqueta)) {
                estadoActual.etiquetasSeleccionadas - etiqueta
            } else {
                estadoActual.etiquetasSeleccionadas + etiqueta
            }
            estadoActual.copy(etiquetasSeleccionadas = nuevasEtiquetas)
        }
    }

    // 6. Publicar y reiniciar reseña
    fun publicarResena() {
        _uiState.update { estadoActual ->
            if (estadoActual.botonPublicarHabilitado) {
                estadoActual.copy(isPublicado = true)
            } else {
                estadoActual
            }
        }
    }

    fun escribirOtraResena() {
        _uiState.value = ResenaUiState() // Reinicia todo a los valores iniciales
    }
}