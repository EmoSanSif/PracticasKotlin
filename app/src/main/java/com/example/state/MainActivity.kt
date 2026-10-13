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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.input.KeyboardType
import kotlin.math.pow

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
    var Num1 by remember { mutableStateOf("") }
    var Num2 by remember { mutableStateOf("") }
    var res by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        // Primer Input
        OutlinedTextField(
            value = Num1,
            onValueChange = { Num1 = it },
            label = { Text("Ingresa el primer numero") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Segundo Input
        OutlinedTextField(
            value = Num2,
            onValueChange = { Num2 = it },
            label = { Text("Ingresa el segundo numero") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Fila 1: Sumar y Restar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    val n1 = Num1.toDoubleOrNull() ?: 0.0
                    val n2 = Num2.toDoubleOrNull() ?: 0.0

                    val suma = n1 + n2
                    res = if (suma % 1.0 == 0.0) {
                        suma.toInt().toString()
                    } else {
                        suma.toString()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Sumar")
            }

            Button(
                onClick = {
                    val n1 = Num1.toDoubleOrNull() ?: 0.0
                    val n2 = Num2.toDoubleOrNull() ?: 0.0

                    val suma = n1 - n2
                    res = if (suma % 1.0 == 0.0) {
                        suma.toInt().toString()
                    } else {
                        suma.toString()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Restar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fila 2: Multiplicar y Dividir
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    val n1 = Num1.toDoubleOrNull() ?: 0.0
                    val n2 = Num2.toDoubleOrNull() ?: 0.0

                    val suma = n1 * n2
                    res = if (suma % 1.0 == 0.0) {
                        suma.toInt().toString()
                    } else {
                        suma.toString()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Multiplicar")
            }

            Button(
                onClick = {
                    val n1 = Num1.toDoubleOrNull() ?: 0.0
                    val n2 = Num2.toDoubleOrNull() ?: 0.0

                    val suma = n1 / n2
                    res = if (suma % 1.0 == 0.0) {
                        suma.toInt().toString()
                    } else {
                        suma.toString()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Dividir")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fila 3: Potencia y Raiz Cuadrada

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    val n1 = Num1.toDoubleOrNull() ?: 0.0
                    val n2 = Num2.toDoubleOrNull() ?: 0.0

                    val suma = n1.pow(n2)
                    res = if (suma % 1.0 == 0.0) {
                        suma.toInt().toString()
                    } else {
                        suma.toString()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Potencia")
            }

            Button(
                onClick = { val n1 = Num1.toDoubleOrNull() ?: 0.0
                    val n2 = Num2.toDoubleOrNull() ?: 0.0

                    val suma = n1.pow((1/n2))
                    res = if (suma % 1.0 == 0.0) {
                        suma.toInt().toString()
                    } else {
                        suma.toString()
                    } },
                modifier = Modifier.weight(1f)
            ) {
                Text("Raiz")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fila 4: Botones Ans e Igual
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {

                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Ans")
            }

            Button(
                onClick = { /* Aquí irá tu lógica */ },
                modifier = Modifier.weight(1f)
            ) {
                Text("Pi")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Input Final: Resultado
        OutlinedTextField(
            value = res,
            onValueChange = {},
            label = { Text("Resultado") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview(showBackground = true)
@Composable
fun InicioSesiomPreview() {

    StateTheme {
        InicioScreen()
    }
}