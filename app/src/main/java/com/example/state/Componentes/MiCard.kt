package com.example.state.Componentes

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt

@Composable
fun MiCard(modifier: Modifier = Modifier, nombre: Color) {
    val colorCian = Color(0xFF59F2D6)
    val colorLima = Color(0xFFC8F969)
    val colorTextoSecundario = Color(0xFF1B3B36)

    // Contenedor principal
    Box(
        modifier = modifier
            .width(360.dp)
            .height(160.dp)
    ) {
        // 1. Fondo de la Tarjeta con Degradado
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp, end = 20.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    color = Color(nombre.value)
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Título del balance
                Text(
                    text = "Color:",
                    color = colorTextoSecundario,
                    fontSize = 14.sp
                )

                // Cantidad principal
                Text(
                    text = nombre.toString(),
                    color = Color.Black,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )

            }
        }
    }
}

