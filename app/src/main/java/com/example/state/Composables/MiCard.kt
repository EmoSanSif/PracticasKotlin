package com.example.state.Composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.state.Clases.Libro

@Composable
fun MiCard(modifier: Modifier, libro: Libro) {
    Box(modifier = Modifier.fillMaxWidth().height(110.dp)
        .border(
            width = 2.dp,
            color = Color.Blue,
            shape = RoundedCornerShape(12.dp)
        ).background(Color.White)
        , contentAlignment = Alignment.Center)
    {
        Column(modifier.fillMaxSize().padding(8.dp)) {
            Text("Titulo: " + libro.titulo)
            Spacer(modifier = Modifier.padding(3.dp))

            Text("Autor: " + libro.autor)
            Spacer(modifier = Modifier.padding(3.dp))

            Text("Numero de paginas: #" + libro.N_paginas)
        }

    }

}