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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.state.ViewModel.BibliotecaViewModel

@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    bibliotecaViewModel: BibliotecaViewModel = viewModel() // 1. Conectamos el ViewModel
) {
    // 2. Observamos el estado (Model) expuesto por el ViewModel
    val uiState by bibliotecaViewModel.uiState.collectAsState()

    // 3. Lógica puramente visual (colores según el porcentaje)
    val colorIndicador = when {
        uiState.porcentajeTexto < 70 -> Color(0xFF2E7D32)
        uiState.porcentajeTexto < 100 -> Color(0xFFF57F17)
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
            text = "Sala de estudio · capacidad ${uiState.capacidadMaxima}",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = TextAlign.End
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
                    text = "${uiState.personasActuales} / ${uiState.capacidadMaxima}",
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
                    progress = { uiState.porcentajeOcupacion },
                    color = colorIndicador,
                    trackColor = Color.LightGray.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "${uiState.porcentajeTexto} % de ocupación",
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

        // Botones Entra / Sale (Solo avisan al ViewModel)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { bibliotecaViewModel.registrarSalida() },
                enabled = uiState.puedeSalir,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text("- Sale", fontSize = 16.sp)
            }

            Button(
                onClick = { bibliotecaViewModel.registrarEntrada() },
                enabled = uiState.puedeEntrar,
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
                    text = "${uiState.visitasDelDia}",
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
            onClick = { bibliotecaViewModel.cerrarDia() },
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