package com.example.state.View

import com.example.state.ViewModel.ResenaViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    resenaViewModel: ResenaViewModel = viewModel() // 1. Conectamos el ViewModel
) {
    // 2. Observamos el estado (Model) expuesto por el ViewModel
    val uiState by resenaViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escribe tu reseña") },
                navigationIcon = {
                    IconButton(onClick = { /* Acción de volver */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (!uiState.isPublicado) {
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
                RatingCategoria(
                    nombre = "Comida",
                    calificacionActual = uiState.califComida,
                    onCalificar = { resenaViewModel.calificarComida(it) }
                )
                RatingCategoria(
                    nombre = "Servicio",
                    calificacionActual = uiState.califServicio,
                    onCalificar = { resenaViewModel.calificarServicio(it) }
                )
                RatingCategoria(
                    nombre = "Ambiente",
                    calificacionActual = uiState.califAmbiente,
                    onCalificar = { resenaViewModel.calificarAmbiente(it) }
                )

                // Mostrar el promedio solo si las 3 están calificadas
                if (uiState.todasCalificadas) {
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
                                    text = String.format(Locale.US, "%.1f", uiState.promedio),
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFB300),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // Campo de Comentario Multilínea
                val contadorColor = if (uiState.alertaContadorCaracteres) Color.Red else Color.Gray

                OutlinedTextField(
                    value = uiState.comentario,
                    onValueChange = { resenaViewModel.actualizarComentario(it) },
                    label = { Text("Comentario") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 4,
                    supportingText = {
                        Text(
                            text = "${uiState.comentario.length}/${uiState.maxCaracteres}",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                            color = contadorColor
                        )
                    }
                )

                // Chips de selección múltiple
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    uiState.etiquetasDisponibles.forEach { etiqueta ->
                        val isSelected = uiState.etiquetasSeleccionadas.contains(etiqueta)
                        FilterChip(
                            selected = isSelected,
                            onClick = { resenaViewModel.alternarEtiqueta(etiqueta) },
                            label = { Text(etiqueta) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón Publicar
                Button(
                    onClick = { resenaViewModel.publicarResena() },
                    enabled = uiState.botonPublicarHabilitado,
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
                Text(
                    text = "¡Reseña Publicada!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("El Mezquite", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Promedio: ${String.format(Locale.US, "%.1f", uiState.promedio)} ★")
                        HorizontalDivider()
                        Text("Comentario:", fontWeight = FontWeight.Medium)
                        Text(uiState.comentario, fontSize = 14.sp)
                        if (uiState.etiquetasSeleccionadas.isNotEmpty()) {
                            HorizontalDivider()
                            Text(
                                text = "Etiquetas: ${uiState.etiquetasSeleccionadas.joinToString(", ")}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedButton(
                    onClick = { resenaViewModel.escribirOtraResena() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
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
fun RatingCategoria(
    nombre: String,
    calificacionActual: Int,
    onCalificar: (Int) -> Unit
) {
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
fun InicioScreenPreview() {
    InicioScreen()
}