package com.example.state.Componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun MiCard(modifier: Modifier, nombre: String) {
    Box(modifier = Modifier.fillMaxWidth().height(70.dp)
        .border(
            width = 2.dp,
            color = Color.Blue,
            shape = RoundedCornerShape(12.dp)
        ).background(Color.White)
        , contentAlignment = Alignment.Center)
    {
        Text(nombre)
    }
    Spacer(modifier = Modifier.padding(5.dp))
}