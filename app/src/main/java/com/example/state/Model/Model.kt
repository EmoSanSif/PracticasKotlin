package com.example.state.Model

data class ResenaUiState(
    // 1. Calificaciones (0 = sin calificar, 1..5 = estrellas)
    val califComida: Int = 0,
    val califServicio: Int = 0,
    val califAmbiente: Int = 0,

    // 2. Comentario y etiquetas
    val comentario: String = "",
    val etiquetasSeleccionadas: Set<String> = emptySet(),
    val etiquetasDisponibles: List<String> = listOf(
        "Familiar", "Económico", "Rápido", "Romántico", "Vegetariano"
    ),

    // 3. Flujo de pantalla
    val isPublicado: Boolean = false
) {
    // Constantes de reglas de negocio
    val minCaracteres: Int get() = 20
    val maxCaracteres: Int get() = 200
    val umbralAlertaCaracteres: Int get() = 180

    // 4. Propiedades derivadas (Lógica pura)
    val todasCalificadas: Boolean
        get() = califComida > 0 && califServicio > 0 && califAmbiente > 0

    val promedio: Double
        get() = if (todasCalificadas) {
            (califComida + califServicio + califAmbiente) / 3.0
        } else {
            0.0
        }

    val isComentarioValido: Boolean
        get() = comentario.trim().length in minCaracteres..maxCaracteres

    val alertaContadorCaracteres: Boolean
        get() = comentario.length >= umbralAlertaCaracteres

    val botonPublicarHabilitado: Boolean
        get() = todasCalificadas && isComentarioValido
}