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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation


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
    // === ESTADOS ===
    // Credenciales fijas
    val correoCorrecto = "paciente@vida.mx"
    val passCorrecta = "Vida2025"

    // Inputs del usuario
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var recordarUsuario by remember { mutableStateOf(false) }

    // Control de flujo de la app
    var intentos by remember { mutableStateOf(0) }
    var mensajeError by remember { mutableStateOf("") }
    var isSesionIniciada by remember { mutableStateOf(false) }

    // Validación para bloquear el botón (tercer intento fallido)
    val isBotonBloqueado = intentos >= 3

    if (!isSesionIniciada) {
        // ==========================================
        // PANTALLA 1: INICIO DE SESIÓN
        // ==========================================
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logotipo decorativo
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Logo",
                tint = Color(0xFF6750A4),
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Clínica Vida", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Consulta tus citas médicas", color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(32.dp))

            // Input: Correo
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input: Contraseña con icono de visibilidad
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val imagenIcono = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = imagenIcono, contentDescription = "Mostrar contraseña")
                    }
                }
            )

            // Checkbox "Recordarme"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clickable { recordarUsuario = !recordarUsuario }
            ) {
                Checkbox(
                    checked = recordarUsuario,
                    onCheckedChange = { recordarUsuario = it }
                )
                Text("Recordarme", fontSize = 14.sp, color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Aviso de error dinámico (Aparece y cambia según la validación)
            if (mensajeError.isNotEmpty()) {
                Surface(
                    color = if (isBotonBloqueado) Color(0xFFFFCDD2) else Color(0xFFFBE9E7),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = mensajeError,
                        color = Color(0xFFC62828),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Botón Entrar
            Button(
                onClick = {
                    if (correo == correoCorrecto && password == passCorrecta) {
                        correo = ""
                        password = ""
                        mensajeError = ""
                        intentos = 0
                        isSesionIniciada = true
                    } else {
                        intentos++
                        correo = ""
                        password = ""

                        if (intentos >= 3) {
                            mensajeError = "Acceso bloqueado por seguridad. Intenta más tarde."
                        } else {
                            mensajeError = "ingreso correctame la cuenta (intento $intentos de 3)"
                        }
                    }
                },
                enabled = !isBotonBloqueado,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6750A4)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Entrar", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Enlace de contraseña decorativo
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = Color(0xFF6750A4),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { /* No hace nada */ }
            )
        }
    } else {
        // ==========================================
        // PANTALLA 2: BIENVENIDA
        // ==========================================
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("¡Bienvenido!", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6750A4))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Has iniciado sesión como:")
            Text(correoCorrecto, fontWeight = FontWeight.Bold, fontSize = 18.sp)

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = { isSesionIniciada = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Cerrar sesión")
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