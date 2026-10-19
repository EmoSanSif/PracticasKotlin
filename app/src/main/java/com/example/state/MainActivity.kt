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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

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
    // 1. Definición del Estado Centralizado
    var fontSize by remember { mutableFloatStateOf(18f) }
    var lineSpacing by remember { mutableFloatStateOf(1.3f) } // 1.0f, 1.3f, 1.6f
    var isDarkMode by remember { mutableStateOf(false) }
    var isSerif by remember { mutableStateOf(true) }

    // Función para restablecer a los valores por defecto
    val restablecerValores = {
        fontSize = 18f
        lineSpacing = 1.3f
        isDarkMode = false
        isSerif = false
    }

    Scaffold() { innerPadding ->
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
                    Text("${fontSize.roundToInt()} sp", color = Color(0xFF6750A4), fontWeight = FontWeight.Bold)
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("A", fontSize = 14.sp)
                    Slider(
                        value = fontSize,
                        onValueChange = { fontSize = it },
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
                    OpcionInterlineado("Compacto", 1.0f, lineSpacing) { lineSpacing = it }
                    OpcionInterlineado("Normal", 1.3f, lineSpacing) { lineSpacing = it }
                    OpcionInterlineado("Amplio", 1.6f, lineSpacing) { lineSpacing = it }
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
                    checked = isDarkMode,
                    onCheckedChange = { isDarkMode = it }
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
                        selected = isSerif,
                        onClick = { isSerif = true },
                        label = { Text("Serif") },

                    )
                    FilterChip(
                        selected = !isSerif,
                        onClick = { isSerif = false },
                        label = { Text("Sans") },

                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // --- SECCIÓN: VISTA PREVIA ---
            Text("VISTA PREVIA", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)

            // Colores dinámicos para la vista previa
            val bgColor = if (isDarkMode) Color(0xFF1E1E1E) else Color(0xFFF5F5F5)
            val textColor = if (isDarkMode) Color.White else Color.Black
            val fontFamily = if (isSerif) FontFamily.Serif else FontFamily.SansSerif

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Toma el espacio restante
                    .clip(RoundedCornerShape(16.dp))
                    .background(bgColor)
                    .padding(24.dp)
            ) {
                Text(
                    text = "Era una noche tranquila en el pueblo cuando las campanas comenzaron a sonar, y nadie supo por qué.",
                    color = textColor,
                    fontSize = fontSize.sp,
                    lineHeight = (fontSize * lineSpacing).sp, // Cálculo del interlineado
                    fontFamily = fontFamily
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- BOTÓN RESTABLECER ---
            OutlinedButton(
                onClick = restablecerValores,
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

// Composable auxiliar para hacer más limpio el código de los RadioButtons
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
            onClick = null // Se maneja en el modifier selectable del Row
        )
        Text(text = texto, fontSize = 14.sp, modifier = Modifier.padding(start = 4.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    InicioScreen()
}