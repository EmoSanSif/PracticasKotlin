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
import androidx.compose.ui.text.input.KeyboardType
import com.example.state.ui.theme.StateTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    // 1. Variables de Estado del Formulario
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var tallerSeleccionado by remember { mutableStateOf("") }
    var privacidadAceptada by remember { mutableStateOf(false) }

    // 2. Variables de Control de Flujo
    var intentoEnvio by remember { mutableStateOf(false) }
    var pantallaConfirmacion by remember { mutableStateOf(false) }
    var dropdownExpandido by remember { mutableStateOf(false) }

    val talleresDisponibles = listOf("Pintura", "Fotografía", "Guitarra")

    // 3. Lógicas de Validación
    val nombreValido = nombre.trim().length >= 3
    val correoValido = correo.contains("@") && correo.substringAfter("@", "").contains(".")
    val edadNum = edad.toIntOrNull() ?: 0
    val edadValida = edadNum in 12..99
    val tallerValido = tallerSeleccionado.isNotBlank()
    val formularioValido = nombreValido && correoValido && edadValida && tallerValido && privacidadAceptada

    if (!pantallaConfirmacion) {
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
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                isError = intentoEnvio && !nombreValido,
                supportingText = {
                    if (intentoEnvio && !nombreValido) Text("Debe tener al menos 3 caracteres")
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo: Correo
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = intentoEnvio && !correoValido,
                supportingText = {
                    if (intentoEnvio && !correoValido) Text("Ingresa un correo válido (ej. usuario@dominio.com)")
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo: Edad
            OutlinedTextField(
                value = edad,
                onValueChange = { edad = it },
                label = { Text("Edad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = intentoEnvio && !edadValida,
                supportingText = {
                    if (intentoEnvio && !edadValida) Text("La edad debe estar entre 12 y 99 años")
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Menú Desplegable: Taller
            ExposedDropdownMenuBox(
                expanded = dropdownExpandido,
                onExpandedChange = { dropdownExpandido = !dropdownExpandido }
            ) {
                OutlinedTextField(
                    value = tallerSeleccionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Taller") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpandido) },
                    isError = intentoEnvio && !tallerValido,
                    supportingText = {
                        if (intentoEnvio && !tallerValido) Text("Selecciona un taller de la lista")
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = dropdownExpandido,
                    onDismissRequest = { dropdownExpandido = false }
                ) {
                    talleresDisponibles.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                tallerSeleccionado = opcion
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
                    .clickable { privacidadAceptada = !privacidadAceptada }
                    .padding(vertical = 8.dp)
            ) {
                Checkbox(
                    checked = privacidadAceptada,
                    onCheckedChange = { privacidadAceptada = it }
                )
                Text(
                    text = "Acepto el aviso de privacidad",
                    color = if (intentoEnvio && !privacidadAceptada) MaterialTheme.colorScheme.error else Color.Unspecified
                )
            }

            // Botón Inscribirme
            Button(
                onClick = {
                    intentoEnvio = true // Registramos que el usuario ya intentó enviar
                    if (formularioValido) {
                        pantallaConfirmacion = true // Cambiamos de pantalla
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)), // Morado
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
                    Text("Nombre: $nombre")
                    Text("Correo: $correo")
                    Text("Edad: $edad años")
                    Text("Taller: $tallerSeleccionado")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Nueva Inscripción
            OutlinedButton(
                onClick = {
                    // Reseteamos absolutamente todo
                    nombre = ""
                    correo = ""
                    edad = ""
                    tallerSeleccionado = ""
                    privacidadAceptada = false
                    intentoEnvio = false
                    pantallaConfirmacion = false
                },
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