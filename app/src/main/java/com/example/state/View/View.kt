package com.example.state.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.state.ViewModel.LecturaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    lecturaViewModel: LecturaViewModel = viewModel() // 1. Conectamos el ViewModel
) {
    // 2. Observamos el estado (Model) expuesto por el ViewModel
    val uiState by lecturaViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes de lectura") },
                navigationIcon = {
                    IconButton(onClick = { /* Acción de volver */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- SECCIÓN: Tamaño de letra ---
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Tamaño de letra", fontWeight = FontWeight.Medium)
                    Text(
                        text = "${uiState.fontSizeEntero} sp",
                        color = Color(0xFF6750A4),
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("A", fontSize = 14.sp)
                    Slider(
                        value = uiState.fontSize,
                        onValueChange = { lecturaViewModel.actualizarTamanoLetra(it) },
                        valueRange = 14f..32f,
                        modifier = Modifier.weight(1f)
                    )
                    Text("A", fontSize = 24.sp)
                }
            }

            // --- SECCIÓN: Interlineado ---
            Column {
                Text("Interlineado", fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectableGroup(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OpcionInterlineado("Compacto", 1.0f, uiState.lineSpacing) {
                        lecturaViewModel.actualizarInterlineado(it)
                    }
                    OpcionInterlineado("Normal", 1.3f, uiState.lineSpacing) {
                        lecturaViewModel.actualizarInterlineado(it)
                    }
                    OpcionInterlineado("Amplio", 1.6f, uiState.lineSpacing) {
                        lecturaViewModel.actualizarInterlineado(it)
                    }
                }
            }

            // --- SECCIÓN: Modo oscuro ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Modo oscuro", fontWeight = FontWeight.Medium)
                Switch(
                    checked = uiState.isDarkMode,
                    onCheckedChange = { lecturaViewModel.cambiarModoOscuro(it) }
                )
            }

            // --- SECCIÓN: Fuente ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Fuente", fontWeight = FontWeight.Medium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = uiState.isSerif,
                        onClick = { lecturaViewModel.cambiarFuente(true) },
                        label = { Text("Serif") },
                        leadingIcon = if (uiState.isSerif) {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        } else null
                    )
                    FilterChip(
                        selected = !uiState.isSerif,
                        onClick = { lecturaViewModel.cambiarFuente(false) },
                        label = { Text("Sans") },
                        leadingIcon = if (!uiState.isSerif) {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        } else null
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // --- SECCIÓN: VISTA PREVIA ---
            Text("VISTA PREVIA", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)

            // Lógica puramente visual derivada del estado
            val bgColor = if (uiState.isDarkMode) Color(0xFF1E1E1E) else Color(0xFFF5F5F5)
            val textColor = if (uiState.isDarkMode) Color.White else Color.Black
            val fontFamily = if (uiState.isSerif) FontFamily.Serif else FontFamily.SansSerif

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(bgColor)
                    .padding(24.dp)
            ) {
                Text(
                    text = "Era una noche tranquila en el pueblo cuando las campanas comenzaron a sonar, y nadie supo por qué.",
                    color = textColor,
                    fontSize = uiState.fontSize.sp,
                    lineHeight = uiState.lineHeightCalculado.sp,
                    fontFamily = fontFamily
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- BOTÓN RESTABLECER ---
            OutlinedButton(
                onClick = { lecturaViewModel.restablecerValores() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Restablecer", color = Color(0xFF6750A4))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun RowScope.OpcionInterlineado(
    texto: String,
    valor: Float,
    valorSeleccionado: Float,
    onSeleccion: (Float) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .selectable(
                selected = (valor == valorSeleccionado),
                onClick = { onSeleccion(valor) },
                role = Role.RadioButton
            )
    ) {
        RadioButton(
            selected = (valor == valorSeleccionado),
            onClick = null
        )
        Text(text = texto, fontSize = 14.sp, modifier = Modifier.padding(start = 4.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    InicioScreen()
}