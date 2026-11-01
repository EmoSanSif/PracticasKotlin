package com.example.state.Clases

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.state.Composables.MiCard

class Libro(val titulo: String, val autor: String, val N_paginas: Int?) {

    @Composable
    fun Describir() {
        MiCard(modifier = Modifier,this)
    }
}