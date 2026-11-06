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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.example.state.Clases.Tarea
import com.example.state.Composables.RowTarea

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
    var mostrar by remember { mutableStateOf("Todas") }
    var cantidad by remember { mutableStateOf(0) }
    val lista = remember { mutableListOf<Tarea> (Tarea("Work", "Alta")) }
    val listaP = remember { mutableListOf<Tarea> () }
    val listaH = remember { mutableListOf<Tarea> () }


    Column(modifier = modifier.fillMaxSize()) {

        // Top bar
        Text(
            text = "Mis Pendientes",
            fontSize = 28.sp,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
        )

        // Row TextField, botton
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
                    if (tarea != "")  {
                        lista.add(Tarea(tarea, prioridad) )
                         }

                },
                modifier = Modifier
                    .weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "+", fontSize = 28.sp)
            }

        }

        Spacer(modifier = Modifier.fillMaxWidth().height(12.dp))

        // Prioridad
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

        Spacer(modifier = Modifier.fillMaxWidth().height(6.dp))

        // Filtros busqueda
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Todas", "Pendientes", "Hechas").forEach { valor ->
                val VariableBool = mostrar == valor
                val textoChip = valor

                FilterChip(
                    selected = VariableBool,
                    onClick = { mostrar = valor },
                    label = { Text(textoChip) }
                )
            }
        }

        Spacer(modifier = Modifier.fillMaxWidth().height(12.dp))

        Divider(
            modifier = Modifier.padding(top = 16.dp),
            thickness = 1.dp,
            color = Color.LightGray.copy(alpha = 0.5f)
        )

        // Rows Tareas
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            if (mostrar == "Todas") {
                items(lista){ valor ->
                    RowTarea(valor, {lista.remove(valor)})
                }
            }

            if (mostrar == "Pendientes") {

                items(lista){ valor ->
                    if(!valor.completada) { listaP.add(valor)}
                }

                items(listaP){ valor ->
                    RowTarea(valor, {lista.remove(valor)})
                }
            }

            if (mostrar == "Hechas") {
                items(lista){ valor ->
                    if(valor.completada) { listaH.add(valor)}
                }

                items(listaH){ valor ->
                    RowTarea(valor, {lista.remove(valor)})
                }
            }

        }

        Spacer(modifier = Modifier.fillMaxWidth().height(14.dp))

        // Pendientes
        Text(
            text = "${listaP.count()} pendientes de ${lista.count()}",
            fontSize = 20.sp,
            color = Color.Black,
        )

        Spacer(modifier = Modifier.fillMaxWidth().height(24.dp))

        // Boton borrar

        Button(
            onClick = {
                lista.forEach {valor -> if(valor.completada) lista.remove(valor) }
            },
            enabled = listaH.count() > 0,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Borrar Completadas")
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