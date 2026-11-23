package com.example.state.ViewModel

import androidx.lifecycle.ViewModel
import com.example.state.Model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    // Credenciales fijas del sistema (encapsuladas en el ViewModel)
    private val correoCorrecto = "paciente@vida.mx"
    private val passCorrecta = "Vida2025"

    // 1. Estado privado mutable
    private val _uiState = MutableStateFlow(LoginUiState())

    // 2. Estado público de solo lectura para la Vista
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // 3. Eventos de entrada del usuario
    fun actualizarCorreo(nuevoCorreo: String) {
        _uiState.update { it.copy(correo = nuevoCorreo) }
    }

    fun actualizarPassword(nuevaPassword: String) {
        _uiState.update { it.copy(password = nuevaPassword) }
    }

    fun cambiarRecordarme(recordar: Boolean) {
        _uiState.update { it.copy(recordarUsuario = recordar) }
    }

    // 4. Lógica de autenticación y seguridad
    fun iniciarSesion() {
        _uiState.update { estadoActual ->
            // Si ya está bloqueado, no hace nada
            if (estadoActual.isBotonBloqueado) return@update estadoActual

            if (estadoActual.correo.trim() == correoCorrecto && estadoActual.password == passCorrecta) {
                // Éxito: Limpia inputs, reinicia intentos y avanza a bienvenida
                estadoActual.copy(
                    correo = "",
                    password = "",
                    mensajeError = "",
                    intentos = 0,
                    isSesionIniciada = true,
                    correoSesionActiva = correoCorrecto
                )
            } else {
                // Fallo: Incrementa intentos, vacía campos y arma el mensaje correspondiente
                val nuevosIntentos = estadoActual.intentos + 1
                val nuevoMensaje = if (nuevosIntentos >= estadoActual.maxIntentos) {
                    "Acceso bloqueado por seguridad. Intenta más tarde."
                } else {
                    "ingreso correctame la cuenta (intento $nuevosIntentos de ${estadoActual.maxIntentos})"
                }

                estadoActual.copy(
                    correo = "",
                    password = "",
                    intentos = nuevosIntentos,
                    mensajeError = nuevoMensaje
                )
            }
        }
    }

    // 5. Cerrar sesión
    fun cerrarSesion() {
        _uiState.update {
            LoginUiState() // Regresa al login con todos los campos limpios
        }
    }
}