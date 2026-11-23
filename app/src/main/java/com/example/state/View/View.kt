package com.example.state.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.state.Model.Tarea
import com.example.state.ViewModel.TareasViewModel

@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    tareasViewModel: TareasViewModel = viewModel() // 1. Conectamos el ViewModel
) {
    // 2. Observamos el estado (Model) expuesto por el ViewModel
    val uiState by tareasViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // Título
        Text(
            text = "Mis pendientes",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )

        // Fila: Campo de texto + Botón agregar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = uiState.textoNuevaTarea,
                onValueChange = { tareasViewModel.actualizarTextoTarea(it) },
                label = { Text("Nueva tarea") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = { tareasViewModel.agregarTarea() },
                enabled = uiState.puedeAgregarTarea, // No permite agregar títulos vacíos
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(56.dp)
            ) {
                Text(text = "+", fontSize = 24.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Selector de Prioridad
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Prioridad", fontSize = 14.sp, color = Color.DarkGray)

            uiState.opcionesPrioridad.forEach { valor ->
                FilterChip(
                    selected = uiState.prioridadSeleccionada == valor,
                    onClick = { tareasViewModel.seleccionarPrioridad(valor) },
                    label = { Text(valor) }
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Filtros de visualización
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.opcionesFiltro.forEach { valor ->
                FilterChip(
                    selected = uiState.filtroActivo == valor,
                    onClick = { tareasViewModel.cambiarFiltro(valor) },
                    label = { Text(valor) }
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp),
            thickness = 1.dp,
            color = Color.LightGray.copy(alpha = 0.5f)
        )

        // Lista dinámica de tareas filtradas
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(
                items = uiState.tareasFiltradas,
                key = { it.id } // Ayuda a Compose a animar y actualizar cada fila por su ID
            ) { tarea ->
                RowTarea(
                    tarea = tarea,
                    onToggleCompletada = { tareasViewModel.alternarCompletada(tarea.id) },
                    onEliminar = { tareasViewModel.eliminarTarea(tarea.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Resumen de pendientes
        Text(
            text = "${uiState.cantidadPendientes} pendientes de ${uiState.totalTareas}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón Borrar Completadas
        OutlinedButton(
            onClick = { tareasViewModel.abrirDialogoBorrar() },
            enabled = uiState.hayCompletadasParaBorrar,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Borrar completadas", color = Color(0xFF6750A4))
        }
    }

    // Diálogo de confirmación controlado por estado
    if (uiState.mostrarDialogoBorrar) {
        AlertDialog(
            onDismissRequest = { tareasViewModel.cerrarDialogoBorrar() },
            title = { Text("Confirmar eliminación") },
            text = {
                Text("¿Seguro que deseas eliminar las ${uiState.cantidadCompletadas} tareas completadas?")
            },
            confirmButton = {
                TextButton(onClick = { tareasViewModel.confirmarBorrarCompletadas() }) {
                    Text("Eliminar", color = Color(0xFFD32F2F))
                }
            },
            dismissButton = {
                TextButton(onClick = { tareasViewModel.cerrarDialogoBorrar() }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// Composable reutilizable para cada fila de tarea
@Composable
fun RowTarea(
    tarea: Tarea,
    onToggleCompletada: () -> Unit,
    onEliminar: () -> Unit
) {
    // Colores visuales según la prioridad
    val (colorFondoEtiqueta, colorTextoEtiqueta) = when (tarea.prioridad) {
        "Alta" -> Color(0xFFFFCDD2) to Color(0xFFB71C1C)
        "Media" -> Color(0xFFFFF9C4) to Color(0xFFF57F17)
        else -> Color(0xFFC8E6C9) to Color(0xFF1B5E20) // Baja
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = tarea.completada,
                onCheckedChange = { onToggleCompletada() }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = tarea.nombre,
                    fontSize = 16.sp,
                    color = if (tarea.completada) Color.Gray else Color.Black,
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Etiqueta de prioridad con color
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(colorFondoEtiqueta)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = tarea.prioridad,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorTextoEtiqueta
                    )
                }
            }

            IconButton(onClick = onEliminar) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Eliminar tarea",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    InicioScreen()
}