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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.input.KeyboardType
import com.example.state.Clases.Libro

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
    var titulo by rememberSaveable { mutableStateOf("") }
    var autor by rememberSaveable { mutableStateOf("") }
    var n_pag by rememberSaveable { mutableStateOf("") }
    var lib by rememberSaveable { mutableStateOf(Libro("","",0)) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Ingresa el titulo del libro") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = autor,
            onValueChange = { autor = it },
            label = { Text("Ingresa el nombre del autor") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = n_pag,
            onValueChange = { n_pag = it },
            label = { Text("Cuantas paginas tiene el libro") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                lib = Libro(titulo, autor, n_pag.toInt())
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("Describir")
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

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