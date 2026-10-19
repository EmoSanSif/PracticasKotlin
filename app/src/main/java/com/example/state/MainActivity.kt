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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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



@Composable
fun InicioScreen(modifier: Modifier = Modifier) {
    var personasActuales by remember { mutableStateOf(0) }
    var visitasDelDia by remember { mutableStateOf(0) }
    val capacidadMaxima = 50

    val porcentajeOcupacion = if (capacidadMaxima > 0) personasActuales.toFloat() / capacidadMaxima else 0f
    val porcentajeTexto = (porcentajeOcupacion * 100).toInt()

    val colorIndicador = when {
        porcentajeTexto < 70 -> Color(0xFF2E7D32)
        porcentajeTexto < 100 -> Color(0xFFF57F17)
        else -> Color(0xFFD32F2F)
    }

    val colorFondoTarjeta = Color(0xFFEDF7ED)
    val colorBotonEntra = Color(0xFF6750A4)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado
        Text(
            text = "Biblioteca Central",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Text(
            text = "Sala de estudio · capacidad $capacidadMaxima",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = TextAlign.Start
        )

        // Tarjeta principal de aforo
        Card(
            colors = CardDefaults.cardColors(containerColor = colorFondoTarjeta),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$personasActuales / $capacidadMaxima",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = colorIndicador
                )
                Text(
                    text = "personas en la sala",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Barra de progreso
                LinearProgressIndicator(
                    progress = porcentajeOcupacion,
                    color = colorIndicador,
                    trackColor = Color.LightGray.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "$porcentajeTexto % de ocupación",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorIndicador
                )
                Text(
                    text = "Aforo disponible",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botones Entra / Sale
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { if (personasActuales > 0) personasActuales-- },
                enabled = personasActuales > 0,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text("- Sale", fontSize = 16.sp)
            }

            Button(
                onClick = {
                    if (personasActuales < capacidadMaxima) {
                        personasActuales++
                        visitasDelDia++
                    }
                },
                enabled = personasActuales < capacidadMaxima,
                colors = ButtonDefaults.buttonColors(containerColor = colorBotonEntra),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text("+ Entra", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tarjeta de Visitas del Día
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Visitas del día", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    Text(text = "Solo suma las entradas", fontSize = 12.sp, color = Color.Gray)
                }
                Text(
                    text = "$visitasDelDia",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Leyenda de colores
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IndicadorLeyenda(color = Color(0xFF2E7D32), texto = "Verde < 70 %")
            IndicadorLeyenda(color = Color(0xFFF57F17), texto = "Amarillo 70–99 %")
            IndicadorLeyenda(color = Color(0xFFD32F2F), texto = "Rojo 100 %")
        }

        Spacer(modifier = Modifier.weight(1f))

        // Botón Cerrar Día
        OutlinedButton(
            onClick = {
                personasActuales = 0
                visitasDelDia = 0
            },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Cerrar día", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun IndicadorLeyenda(color: Color, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = texto, fontSize = 10.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    InicioScreen()
}