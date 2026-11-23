package com.example.state.Model

data class LoginUiState(
    // 1. Datos de entrada
    val correo: String = "",
    val password: String = "",
    val recordarUsuario: Boolean = false,

    // 2. Control de seguridad y navegación
    val intentos: Int = 0,
    val mensajeError: String = "",
    val isSesionIniciada: Boolean = false,
    val correoSesionActiva: String = ""
) {
    // 3. Reglas derivadas (Lógica pura de seguridad)
    val maxIntentos: Int
        get() = 3

    val isBotonBloqueado: Boolean
        get() = intentos >= maxIntentos

    val hayError: Boolean
        get() = mensajeError.isNotEmpty()
}