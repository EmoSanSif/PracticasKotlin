package com.example.state.View

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.state.Model.PantallaQuiz
import com.example.state.Model.QuizUiState
import com.example.state.ViewModel.QuizViewModel

@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    quizViewModel: QuizViewModel = viewModel() // 1. Conectamos el ViewModel
) {
    // 2. Observamos el estado global expuesto por el ViewModel
    val uiState by quizViewModel.uiState.collectAsState()

    when (uiState.pantallaActual) {
        PantallaQuiz.INICIO -> {
            PantallaInicio(
                nombre = uiState.nombreJugador,
                isNombreValido = uiState.isNombreValido,
                onNombreChange = { quizViewModel.actualizarNombre(it) },
                onComenzar = { quizViewModel.comenzarQuiz() },
                modifier = modifier
            )
        }
        PantallaQuiz.JUEGO -> {
            PantallaPregunta(
                uiState = uiState,
                onSeleccionarOpcion = { quizViewModel.seleccionarOpcion(it) },
                onConfirmar = { quizViewModel.confirmarRespuesta() },
                onSiguiente = { quizViewModel.siguientePregunta() },
                modifier = modifier
            )
        }
        PantallaQuiz.RESULTADOS -> {
            PantallaResultados(
                nombre = uiState.nombreJugador,
                puntaje = uiState.puntaje,
                total = uiState.totalPreguntas,
                mensaje = uiState.mensajeFinal,
                onReintentar = { quizViewModel.reintentar() },
                onCambiarJugador = { quizViewModel.cambiarJugador() },
                modifier = modifier
            )
        }
    }
}

// ==========================================
// PANTALLA 1: INICIO (100% Stateless)
// ==========================================
@Composable
fun PantallaInicio(
    nombre: String,
    isNombreValido: Boolean,
    onNombreChange: (String) -> Unit,
    onComenzar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Quiz de programación", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Fundamentos - 5 preguntas", color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = onNombreChange,
            label = { Text("Tu nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onComenzar,
            enabled = isNombreValido,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Comenzar")
        }
    }
}

// ==========================================
// PANTALLA 2: PREGUNTA (100% Stateless)
// ==========================================
@Composable
fun PantallaPregunta(
    uiState: QuizUiState,
    onSeleccionarOpcion: (Int) -> Unit,
    onConfirmar: () -> Unit,
    onSiguiente: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pregunta = uiState.preguntaActual

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado
        Text("Quiz de programación", fontSize = 20.sp, fontWeight = FontWeight.Medium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Pregunta ${uiState.numeroPreguntaActual} de ${uiState.totalPreguntas}",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Text(
                text = "Puntaje: ${uiState.puntaje}",
                color = Color(0xFF6750A4),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        LinearProgressIndicator(
            progress = { uiState.progreso },
            color = Color(0xFF6750A4),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Enunciado
        Text(pregunta.enunciado, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        // Opciones
        pregunta.opciones.forEachIndexed { index, textoOpcion ->
            val isSelected = uiState.opcionSeleccionada == index
            val isCorrecta = index == pregunta.indiceCorrecta

            val colorFondo = when {
                !uiState.isConfirmado -> if (isSelected) Color(0xFFF3E5F5) else Color.Transparent
                uiState.isConfirmado && isCorrecta -> Color(0xFFE8F5E9) // Verde (Correcta)
                uiState.isConfirmado && isSelected && !isCorrecta -> Color(0xFFFFEBEE) // Rojo (Incorrecta)
                else -> Color.Transparent
            }

            val colorBorde = when {
                !uiState.isConfirmado -> if (isSelected) Color(0xFF6750A4) else Color.LightGray
                uiState.isConfirmado && isCorrecta -> Color(0xFF4CAF50)
                uiState.isConfirmado && isSelected && !isCorrecta -> Color(0xFFE53935)
                else -> Color.LightGray
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = colorFondo),
                border = BorderStroke(1.dp, colorBorde),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !uiState.isConfirmado) {
                        onSeleccionarOpcion(index)
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = null
                        )
                        Text(textoOpcion, modifier = Modifier.padding(start = 8.dp))
                    }

                    // Iconos de feedback al confirmar
                    if (uiState.isConfirmado) {
                        if (isCorrecta) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF4CAF50))
                        } else if (isSelected) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = Color(0xFFE53935))
                        }
                    }
                }
            }
        }

        // Mensaje de retroalimentación
        if (uiState.mostrarRetroalimentacionError) {
            Surface(
                color = Color(0xFFFFEBEE),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = pregunta.retroalimentacion,
                    color = Color(0xFFC62828),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botones de acción
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = onConfirmar,
                enabled = uiState.puedeConfirmar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE8DEF8),
                    contentColor = Color(0xFF6750A4)
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text("Confirmar")
            }

            Button(
                onClick = onSiguiente,
                enabled = uiState.isConfirmado,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text(if (uiState.isUltimaPregunta) "Ver resultados" else "Siguiente")
            }
        }
    }
}

// ==========================================
// PANTALLA 3: RESULTADOS (100% Stateless)
// ==========================================
@Composable
fun PantallaResultados(
    nombre: String,
    puntaje: Int,
    total: Int,
    mensaje: String,
    onReintentar: () -> Unit,
    onCambiarJugador: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡$mensaje, $nombre!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "$puntaje de $total",
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF6750A4)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onReintentar,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Reintentar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onCambiarJugador,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Cambiar jugador", color = Color(0xFF6750A4))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    InicioScreen()
}