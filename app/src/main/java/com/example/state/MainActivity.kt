package com.example.state

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.example.state.Clases.Tarea

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StateTheme {
                Scaffold (modifier = Modifier.fillMaxSize()) { innerPadding ->

                    InicioScreen(modifier = Modifier.padding(innerPadding).padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun InicioScreen(modifier: Modifier = Modifier) {
    var tarea by remember { mutableStateOf("") }
    var prioridad by remember { mutableStateOf("Alta") }
    val mostrar by remember { mutableStateOf("Todas") }
    val cantidad by remember { mutableStateOf(0) }
    val lista = remember { mutableListOf<Tarea> () }


    Column(modifier = modifier.fillMaxSize()) {

        Text(
            text = "Mis Pendientes",
            fontSize = 28.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
        )

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            OutlinedTextField(
                value = tarea,
                onValueChange = { tarea = it },
                label = { Text("Nueva tarea") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
            )

            Button(
                onClick = {
                    if (tarea != "") lista.add(Tarea(tarea, prioridad))
                },
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "+", fontSize = 28.sp)
            }

        }

        Spacer(modifier = Modifier.fillMaxWidth().height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Prioridad",
                fontSize = 14.sp,
                color = Color.Black,
            )

            listOf("Alta", "Media", "Baja").forEach { valor ->
                val VariableBool = prioridad == valor
                val textoChip = valor

                FilterChip(
                    selected = VariableBool,
                    onClick = { prioridad = valor },
                    label = { Text(textoChip) }
                )
            }
        }

        Spacer(modifier = Modifier.fillMaxWidth().height(12.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {

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