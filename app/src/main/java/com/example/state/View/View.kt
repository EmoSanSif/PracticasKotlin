package com.example.state.View

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.state.ViewModel.LoginViewModel

@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel() // 1. Conectamos el ViewModel
) {
    // 2. Observamos el estado (Model) expuesto por el ViewModel
    val uiState by loginViewModel.uiState.collectAsState()

    // 3. Único estado local en la View: visibilidad visual de la contraseña
    var passwordVisible by remember { mutableStateOf(false) }

    if (!uiState.isSesionIniciada) {
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
                value = uiState.correo,
                onValueChange = { loginViewModel.actualizarCorreo(it) },
                label = { Text("Correo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input: Contraseña con icono de visibilidad
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { loginViewModel.actualizarPassword(it) },
                label = { Text("Contraseña") },
                singleLine = true,
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
                    .clickable { loginViewModel.cambiarRecordarme(!uiState.recordarUsuario) }
            ) {
                Checkbox(
                    checked = uiState.recordarUsuario,
                    onCheckedChange = { loginViewModel.cambiarRecordarme(it) }
                )
                Text("Recordarme", fontSize = 14.sp, color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Aviso de error dinámico
            if (uiState.hayError) {
                Surface(
                    color = if (uiState.isBotonBloqueado) Color(0xFFFFCDD2) else Color(0xFFFBE9E7),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = uiState.mensajeError,
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
                onClick = { loginViewModel.iniciarSesion() },
                enabled = !uiState.isBotonBloqueado,
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
            Text(
                text = "¡Bienvenido!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6750A4)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Has iniciado sesión como:")
            Text(
                text = uiState.correoSesionActiva,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = { loginViewModel.cerrarSesion() },
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
fun InicioScreenPreview() {
    InicioScreen()
}