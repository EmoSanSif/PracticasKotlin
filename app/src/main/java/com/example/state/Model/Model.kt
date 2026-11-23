package com.example.state.Model

// 1. Estructuras base del dominio
data class Pregunta(
    val enunciado: String,
    val opciones: List<String>,
    val indiceCorrecta: Int,
    val retroalimentacion: String
)

enum class PantallaQuiz { INICIO, JUEGO, RESULTADOS }

// 2. Estado completo de la aplicación de Quiz
data class QuizUiState(
    val pantallaActual: PantallaQuiz = PantallaQuiz.INICIO,
    val nombreJugador: String = "",
    val preguntas: List<Pregunta> = listOf(
        Pregunta(
            "¿Qué tipo de dato conviene usar para guardar el valor 3.14?",
            listOf("Int", "Double", "String", "Boolean"),
            1,
            "Incorrecto. La respuesta correcta es Double."
        ),
        Pregunta(
            "¿Qué palabra reservada se usa para declarar una variable inmutable en Kotlin?",
            listOf("var", "let", "val", "const"),
            2,
            "Incorrecto. La respuesta correcta es 'val'."
        ),
        Pregunta(
            "¿Cuál es el símbolo del operador lógico AND?",
            listOf("||", "&&", "==", "!="),
            1,
            "Incorrecto. La respuesta correcta es &&."
        ),
        Pregunta(
            "¿Cómo se inicializa un arreglo vacío de enteros en Kotlin?",
            listOf("[]", "Array()", "arrayOf()", "intArrayOf()"),
            3,
            "Incorrecto. La respuesta correcta es intArrayOf()."
        ),
        Pregunta(
            "¿Qué significa POO?",
            listOf("Programación Orientada a Objetos", "Procesamiento de Operaciones", "Paradigmas Orientados", "Ninguna"),
            0,
            "Incorrecto. Significa Programación Orientada a Objetos."
        )
    ),
    val indicePregunta: Int = 0,
    val puntaje: Int = 0,
    val opcionSeleccionada: Int = -1,
    val isConfirmado: Boolean = false
) {
    // 3. Propiedades derivadas (Reglas de negocio puras)
    val isNombreValido: Boolean
        get() = nombreJugador.trim().length >= 2

    val totalPreguntas: Int
        get() = preguntas.size

    val preguntaActual: Pregunta
        get() = preguntas[indicePregunta]

    val numeroPreguntaActual: Int
        get() = indicePregunta + 1

    val progreso: Float
        get() = numeroPreguntaActual.toFloat() / totalPreguntas

    val isUltimaPregunta: Boolean
        get() = numeroPreguntaActual == totalPreguntas

    val puedeConfirmar: Boolean
        get() = opcionSeleccionada != -1 && !isConfirmado

    val acertoPreguntaActual: Boolean
        get() = opcionSeleccionada == preguntaActual.indiceCorrecta

    val mostrarRetroalimentacionError: Boolean
        get() = isConfirmado && !acertoPreguntaActual

    val mensajeFinal: String
        get() = when (puntaje) {
            totalPreguntas -> "¡Perfecto!"
            in 3..4 -> "Buen trabajo"
            else -> "Sigue practicando"
        }
}