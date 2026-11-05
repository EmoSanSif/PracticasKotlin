package com.example.state

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.state.ui.theme.StateTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StateTheme {
                Scaffold (modifier = Modifier.fillMaxSize()) { innerPadding ->

                    InicioScreen(modifier = Modifier.padding(innerPadding).padding(horizontal = 20.dp))
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(modifier: Modifier = Modifier) {
        // 1. Estados de las calificaciones (1 a 5, 0 significa sin calificar)
        var califComida by remember { mutableIntStateOf(0) }
        var califServicio by remember { mutableIntStateOf(0) }
        var califAmbiente by remember { mutableIntStateOf(0) }

        // 2. Estado del comentario y sus validaciones
        var comentario by remember { mutableStateOf("") }
        val maxCaracteres = 200
        val minCaracteres = 20
        val isComentarioValido = comentario.length in minCaracteres..maxCaracteres

        // 3. Estado de los Chips (Selección múltiple usando un Set)
        val etiquetasDisponibles = listOf("Familiar", "Económico", "Rápido", "Romántico", "Vegetariano")
        var etiquetasSeleccionadas by remember { mutableStateOf(setOf<String>()) }

        // 4. Estado de Publicación
        var isPublicado by remember { mutableStateOf(false) }

        // Cálculos derivados
        val todasCalificadas = califComida > 0 && califServicio > 0 && califAmbiente > 0
        val botonHabilitado = todasCalificadas && isComentarioValido

        val promedio = if (todasCalificadas) {
            (califComida + califServicio + califAmbiente) / 3.0
        } else 0.0

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Escribe tu reseña") },
                    navigationIcon = {
                        IconButton(onClick = { /* Acción de volver */ }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { innerPadding ->
            if (!isPublicado) {
                // ==========================================
                // PANTALLA 1: FORMULARIO DE RESEÑA
                // ==========================================
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Encabezado
                    Column {
                        Text("Restaurante El Mezquite", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Califica tu experiencia", fontSize = 12.sp, color = Color.Gray)
                    }

                    // Sección de Calificaciones usando el componente reutilizable
                    RatingCategoria("Comida", califComida) { califComida = it }
                    RatingCategoria("Servicio", califServicio) { califServicio = it }
                    RatingCategoria("Ambiente", califAmbiente) { califAmbiente = it }

                    // Mostrar el promedio solo si las 3 están calificadas
                    if (todasCalificadas) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Calificación general", fontSize = 14.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = String.format(Locale.US, "%.1f", promedio),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }

                    // Campo de Comentario Multilínea
                    val contadorColor = if (comentario.length >= 180) Color.Red else Color.Gray

                    OutlinedTextField(
                        value = comentario,
                        onValueChange = {
                            if (it.length <= maxCaracteres) comentario = it
                        },
                        label = { Text("Comentario") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        maxLines = 4,
                        supportingText = {
                            Text(
                                text = "${comentario.length}/$maxCaracteres",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = androidx.compose.ui.text.style.TextAlign.End,
                                color = contadorColor
                            )
                        }
                    )

                    // Chips de selección múltiple (Optimizados con FlowRow en Compose material3)
                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        etiquetasDisponibles.forEach { etiqueta ->
                            val isSelected = etiquetasSeleccionadas.contains(etiqueta)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    etiquetasSeleccionadas = if (isSelected) {
                                        etiquetasSeleccionadas - etiqueta
                                    } else {
                                        etiquetasSeleccionadas + etiqueta
                                    }
                                },
                                label = { Text(etiqueta) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botón Publicar
                    Button(
                        onClick = { isPublicado = true },
                        enabled = botonHabilitado,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Publicar")
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            } else {
                // ==========================================
                // PANTALLA 2: RESUMEN DE LA RESEÑA
                // ==========================================
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("¡Reseña Publicada!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("El Mezquite", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("Promedio: ${String.format(Locale.US, "%.1f", promedio)} ★")
                            Divider()
                            Text("Comentario:", fontWeight = FontWeight.Medium)
                            Text(comentario, fontSize = 14.sp)
                            if (etiquetasSeleccionadas.isNotEmpty()) {
                                Divider()
                                Text("Etiquetas: ${etiquetasSeleccionadas.joinToString(", ")}", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedButton(
                        onClick = {
                            // Reiniciar todo
                            califComida = 0; califServicio = 0; califAmbiente = 0
                            comentario = ""
                            etiquetasSeleccionadas = setOf()
                            isPublicado = false
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("Escribir otra reseña")
                    }
                }
            }
        }
    }

    // ==========================================
// COMPONENTE REUTILIZABLE: RATING BAR
// ==========================================
    @Composable
    fun RatingCategoria(nombre: String, calificacionActual: Int, onCalificar: (Int) -> Unit) {
        val textosDescriptivos = listOf("", "Malo", "Regular", "Bueno", "Muy bueno", "Excelente")

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(nombre, fontSize = 14.sp, color = Color.DarkGray)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Estrellas
                Row {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= calificacionActual) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Calificar $i",
                            tint = if (i <= calificacionActual) Color(0xFFFFB300) else Color.LightGray,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { onCalificar(i) }
                                .padding(2.dp)
                        )
                    }
                }

                // Texto descriptivo dinámico
                if (calificacionActual > 0) {
                    Text(
                        text = textosDescriptivos[calificacionActual],
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6750A4)
                    )
                }
            }
        }
}



@Preview(showBackground = true)
@Composable
fun InicioSesiomPreview() {

    StateTheme {
        InicioScreen()
    }
}