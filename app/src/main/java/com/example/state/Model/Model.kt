package com.example.state.Model

data class InscripcionUiState(
    // 1. Datos del formulario
    val nombre: String = "",
    val correo: String = "",
    val edad: String = "",
    val tallerSeleccionado: String = "",
    val privacidadAceptada: Boolean = false,

    // 2. Control de flujo
    val intentoEnvio: Boolean = false,
    val pantallaConfirmacion: Boolean = false,
    val talleresDisponibles: List<String> = listOf("Pintura", "Fotografía", "Guitarra")
) {
    // 3. Validaciones derivadas (Lógica pura de negocio)
    val nombreValido: Boolean
        get() = nombre.trim().length >= 3

    val correoValido: Boolean
        get() = correo.contains("@") && correo.substringAfter("@", "").contains(".")

    val edadNum: Int
        get() = edad.toIntOrNull() ?: 0

    val edadValida: Boolean
        get() = edadNum in 12..99

    val tallerValido: Boolean
        get() = tallerSeleccionado.isNotBlank()

    val formularioValido: Boolean
        get() = nombreValido && correoValido && edadValida && tallerValido && privacidadAceptada

    // 4. Banderas listas para que la View sepa si pintar rojo o no
    val mostrarErrorNombre: Boolean get() = intentoEnvio && !nombreValido
    val mostrarErrorCorreo: Boolean get() = intentoEnvio && !correoValido
    val mostrarErrorEdad: Boolean get() = intentoEnvio && !edadValida
    val mostrarErrorTaller: Boolean get() = intentoEnvio && !tallerValido
    val mostrarErrorPrivacidad: Boolean get() = intentoEnvio && !privacidadAceptada
}