package com.example.state.Clases

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Tarea(val Nombre: String, val Prioridad: String, estado: Boolean = false) {
    var completada by mutableStateOf(estado)
}