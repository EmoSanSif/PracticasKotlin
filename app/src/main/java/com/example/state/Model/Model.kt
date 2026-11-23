package com.example.state.Model

import kotlin.math.roundToInt

data class LecturaUiState(
    val fontSize: Float = 18f,
    val lineSpacing: Float = 1.3f,
    val isDarkMode: Boolean = false,
    val isSerif: Boolean = false
) {
    // Propiedades calculadas (lógica pura, sin cosas visuales como Color o FontFamily)
    val fontSizeEntero: Int
        get() = fontSize.roundToInt()

    val lineHeightCalculado: Float
        get() = fontSize * lineSpacing
}