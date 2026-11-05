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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import kotlin.math.ceil

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
    var montoCuenta by remember { mutableStateOf("") }
    var propinaSeleccionada by remember { mutableStateOf(10) }
    var propinaPersonalizada by remember { mutableFloatStateOf(0f) }
    var numeroPersonas by remember { mutableIntStateOf(1) }
    var redondearArriba by remember { mutableStateOf(true) }

    //  Logica y calculos
    val montoSeguro = montoCuenta.toDoubleOrNull() ?: 0.0
    val esMontoInvalido = montoCuenta.isNotEmpty() && montoCuenta.toDoubleOrNull() == null

    val porcentajeAplicar = if (propinaSeleccionada == -1) propinaPersonalizada.toDouble() else propinaSeleccionada.toDouble()
    val montoPropina = montoSeguro * (porcentajeAplicar / 100)
    val totalConPropina = montoSeguro + montoPropina

    val montoPersonaCrudo = totalConPropina / numeroPersonas
    val montoPersonaFinal = if (redondearArriba) ceil(montoPersonaCrudo) else montoPersonaCrudo

    val sobranteRedondeo = (montoPersonaFinal * numeroPersonas) - totalConPropina


    fun formatoMoneda(valor: Double): String = String.format("$%,.2f", valor)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Cuenta dividida", fontSize = 24.sp, fontWeight = FontWeight.Normal)

        // Campo de Monto
        OutlinedTextField(
            value = montoCuenta,
            onValueChange = { montoCuenta = it },
            label = { Text("Monto de la cuenta") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = esMontoInvalido,
            supportingText = { if (esMontoInvalido) Text("Ingresa solo números válidos") },
            modifier = Modifier.fillMaxWidth()
        )

        // Sección Propina
        Column {
            Text("Propina", color = Color.DarkGray, fontSize = 14.sp)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(10, 15, 20, -1).forEach { valor ->
                    val esSeleccionado = propinaSeleccionada == valor
                    val textoChip = if (valor == -1) "Otro" else "$valor %"

                    FilterChip(
                        selected = esSeleccionado,
                        onClick = { propinaSeleccionada = valor },
                        label = { Text(textoChip) }
                    )
                }
            }

            // Slider oculto
            if (propinaSeleccionada == -1) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Personalizado:")
                        Text("${propinaPersonalizada.toInt()} %", fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = propinaPersonalizada,
                        onValueChange = { propinaPersonalizada = it },
                        valueRange = 0f..30f
                    )
                }
            }
        }

        Divider(color = Color.LightGray.copy(alpha = 0.5f))

        // Sección Personas
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Personas", color = Color.DarkGray, fontSize = 14.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = { if (numeroPersonas > 1) numeroPersonas-- },
                    modifier = Modifier.size(40.dp),
                    contentPadding = PaddingValues(0.dp)
                ) { Text("-", fontSize = 20.sp) }

                Text("$numeroPersonas", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))

                OutlinedButton(
                    onClick = { if (numeroPersonas < 20) numeroPersonas++ },
                    modifier = Modifier.size(40.dp),
                    contentPadding = PaddingValues(0.dp)
                ) { Text("+", fontSize = 20.sp) }
            }
        }

        // Redondear
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Redondear por persona", color = Color.DarkGray, fontSize = 14.sp)
            Switch(
                checked = redondearArriba,
                onCheckedChange = { redondearArriba = it }
            )
        }

        // Tarjeta de Resultados
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F3FB)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                // Detalles arriba
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Propina (${porcentajeAplicar.toInt()} %)", color = Color.Gray, fontSize = 12.sp)
                    Text(formatoMoneda(montoPropina), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total con propina", color = Color.Gray, fontSize = 12.sp)
                    Text(formatoMoneda(totalConPropina), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))

                // Gran total al centro
                Text("Cada quien paga", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Text(
                    text = formatoMoneda(montoPersonaFinal),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6750A4),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text("entre $numeroPersonas personas", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)

                if (redondearArriba) {
                    Text("Sobrante por redondeo: ${formatoMoneda(sobranteRedondeo)}", color = Color.Gray, fontSize = 10.sp, modifier = Modifier.fillMaxWidth().padding(top = 4.dp), textAlign = TextAlign.Center)
                }
            }
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