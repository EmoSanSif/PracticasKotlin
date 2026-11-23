package com.example.state.View

import com.example.state.ViewModel.InscripcionViewModel
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    inscripcionViewModel: InscripcionViewModel = viewModel() // 1. Conectamos el ViewModel
) {
    // 2. Observamos el estado del formulario y validaciones (Model)
    val uiState by inscripcionViewModel.uiState.collectAsState()

    // 3. Único estado local permitido: control visual de si el menú desplegable está abierto
    var dropdownExpandido by remember { mutableStateOf(false) }

    if (!uiState.pantallaConfirmacion) {
        // ==========================================
        // PANTALLA 1: FORMULARIO
        // ==========================================
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Encabezados
            Text(text = "Inscripción a talleres", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = "Casa del Lago · cupo limitado", color = Color.Gray, fontSize = 14.sp)

            // Campo: Nombre
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { inscripcionViewModel.actualizarNombre(it) },
                label = { Text("Nombre completo") },
                isError = uiState.mostrarErrorNombre,
                supportingText = {
                    if (uiState.mostrarErrorNombre) Text("Debe tener al menos 3 caracteres")
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo: Correo
            OutlinedTextField(
                value = uiState.correo,
                onValueChange = { inscripcionViewModel.actualizarCorreo(it) },
                label = { Text("Correo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = uiState.mostrarErrorCorreo,
                supportingText = {
                    if (uiState.mostrarErrorCorreo) Text("Ingresa un correo válido (ej. usuario@dominio.com)")
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo: Edad
            OutlinedTextField(
                value = uiState.edad,
                onValueChange = { inscripcionViewModel.actualizarEdad(it) },
                label = { Text("Edad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = uiState.mostrarErrorEdad,
                supportingText = {
                    if (uiState.mostrarErrorEdad) Text("La edad debe estar entre 12 y 99 años")
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Menú Desplegable: Taller
            ExposedDropdownMenuBox(
                expanded = dropdownExpandido,
                onExpandedChange = { dropdownExpandido = !dropdownExpandido }
            ) {
                OutlinedTextField(
                    value = uiState.tallerSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Taller") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpandido) },
                    isError = uiState.mostrarErrorTaller,
                    supportingText = {
                        if (uiState.mostrarErrorTaller) Text("Selecciona un taller de la lista")
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = dropdownExpandido,
                    onDismissRequest = { dropdownExpandido = false }
                ) {
                    uiState.talleresDisponibles.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                inscripcionViewModel.seleccionarTaller(opcion)
                                dropdownExpandido = false
                            }
                        )
                    }
                }
            }

            // Casilla de Privacidad
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { inscripcionViewModel.cambiarPrivacidad(!uiState.privacidadAceptada) }
                    .padding(vertical = 8.dp)
            ) {
                Checkbox(
                    checked = uiState.privacidadAceptada,
                    onCheckedChange = { inscripcionViewModel.cambiarPrivacidad(it) }
                )
                Text(
                    text = "Acepto el aviso de privacidad",
                    color = if (uiState.mostrarErrorPrivacidad) MaterialTheme.colorScheme.error else Color.Unspecified
                )
            }

            // Botón Inscribirme
            Button(
                onClick = { inscripcionViewModel.intentarInscripcion() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Inscribirme", fontSize = 16.sp)
            }
        }
    } else {
        // ==========================================
        // PANTALLA 2: CONFIRMACIÓN
        // ==========================================
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "¡Inscripción Exitosa!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))

            // Resumen de datos
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Resumen de inscripción:", fontWeight = FontWeight.Bold)
                    Text("Nombre: ${uiState.nombre}")
                    Text("Correo: ${uiState.correo}")
                    Text("Edad: ${uiState.edad} años")
                    Text("Taller: ${uiState.tallerSeleccionado}")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Nueva Inscripción
            OutlinedButton(
                onClick = { inscripcionViewModel.nuevaInscripcion() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Nueva inscripción", fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    InicioScreen()
}