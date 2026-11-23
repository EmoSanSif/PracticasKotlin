package com.example.state.ViewModel

import com.example.state.Model.QuizUiState
import androidx.lifecycle.ViewModel
import com.example.state.Model.PantallaQuiz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QuizViewModel : ViewModel() {

    // 1. Estado privado mutable
    private val _uiState = MutableStateFlow(QuizUiState())

    // 2. Estado público de solo lectura para la Vista
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    // 3. Pantalla de Inicio
    fun actualizarNombre(nuevoNombre: String) {
        _uiState.update { it.copy(nombreJugador = nuevoNombre) }
    }

    fun comenzarQuiz() {
        _uiState.update { estadoActual ->
            if (estadoActual.isNombreValido) {
                estadoActual.copy(
                    pantallaActual = PantallaQuiz.JUEGO,
                    indicePregunta = 0,
                    puntaje = 0,
                    opcionSeleccionada = -1,
                    isConfirmado = false
                )
            } else {
                estadoActual
            }
        }
    }

    // 4. Pantalla de Pregunta (Flujo pedagógico en 2 pasos)
    fun seleccionarOpcion(indice: Int) {
        _uiState.update { estadoActual ->
            // Solo permite cambiar la selección si aún no ha confirmado
            if (!estadoActual.isConfirmado) {
                estadoActual.copy(opcionSeleccionada = indice)
            } else {
                estadoActual
            }
        }
    }

    fun confirmarRespuesta() {
        _uiState.update { estadoActual ->
            if (estadoActual.puedeConfirmar) {
                val nuevoPuntaje = if (estadoActual.acertoPreguntaActual) {
                    estadoActual.puntaje + 1
                } else {
                    estadoActual.puntaje
                }
                estadoActual.copy(
                    isConfirmado = true,
                    puntaje = nuevoPuntaje
                )
            } else {
                estadoActual
            }
        }
    }

    fun siguientePregunta() {
        _uiState.update { estadoActual ->
            if (!estadoActual.isConfirmado) return@update estadoActual

            if (estadoActual.indicePregunta < estadoActual.totalPreguntas - 1) {
                // Avanza a la siguiente pregunta y resetea la selección/confirmación
                estadoActual.copy(
                    indicePregunta = estadoActual.indicePregunta + 1,
                    opcionSeleccionada = -1,
                    isConfirmado = false
                )
            } else {
                // Terminó el quiz -> pasa a resultados
                estadoActual.copy(pantallaActual = PantallaQuiz.RESULTADOS)
            }
        }
    }

    // 5. Pantalla de Resultados
    fun reintentar() {
        _uiState.update { estadoActual ->
            // Conserva nombreJugador, pero reinicia puntaje, índice y estados de pregunta
            estadoActual.copy(
                pantallaActual = PantallaQuiz.JUEGO,
                indicePregunta = 0,
                puntaje = 0,
                opcionSeleccionada = -1,
                isConfirmado = false
            )
        }
    }

    fun cambiarJugador() {
        // Resetea absolutamente todo al estado inicial (incluyendo nombreJugador)
        _uiState.value = QuizUiState()
    }
}