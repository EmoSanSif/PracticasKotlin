package com.example.state.View

import com.example.state.ViewModel.CuentaDivididaViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale

@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    cuentaViewModel: CuentaDivididaViewModel = viewModel() // 1. Conectamos el ViewModel
) {
    // 2. Observamos el estado (Model) expuesto por el ViewModel
    val uiState by cuentaViewModel.uiState.collectAsState()

    // Función auxiliar puramente visual para formatear dinero en pantalla
    fun formatoMoneda(valor: Double): String = String.format(Locale.US, "$%,.2f", valor)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Cuenta dividida", fontSize = 24.sp, fontWeight = FontWeight.Normal)

        // Campo de Monto
        OutlinedTextField(
            value = uiState.montoCuenta,
            onValueChange = { cuentaViewModel.actualizarMonto(it) },
            label = { Text("Monto de la cuenta") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = uiState.esMontoInvalido,
            supportingText = {
                if (uiState.esMontoInvalido) Text("Ingresa solo números válidos")
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Sección Propina
        Column {
            Text("Propina", color = Color.DarkGray, fontSize = 14.sp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiState.opcionesPropina.forEach { valor ->
                    val esSeleccionado = uiState.propinaSeleccionada == valor
                    val textoChip = if (valor == -1) "Otro" else "$valor %"

                    FilterChip(
                        selected = esSeleccionado,
                        onClick = { cuentaViewModel.seleccionarPropina(valor) },
                        label = { Text(textoChip) }
                    )
                }
            }

            // Slider visible solo si se eligió "Otro"
            if (uiState.esPropinaPersonalizada) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Personalizado:")
                        Text("${uiState.propinaPersonalizada.toInt()} %", fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = uiState.propinaPersonalizada,
                        onValueChange = { cuentaViewModel.actualizarPropinaPersonalizada(it) },
                        valueRange = 0f..30f
                    )
                }
            }
        }

        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))

        // Sección Personas (Stepper)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Personas", color = Color.DarkGray, fontSize = 14.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = { cuentaViewModel.restarPersona() },
                    enabled = uiState.puedeRestarPersona,
                    modifier = Modifier.size(40.dp),
                    contentPadding = PaddingValues(0.dp)
                ) { Text("-", fontSize = 20.sp) }

                Text(
                    text = "${uiState.numeroPersonas}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                OutlinedButton(
                    onClick = { cuentaViewModel.sumarPersona() },
                    enabled = uiState.puedeSumarPersona,
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
                checked = uiState.redondearArriba,
                onCheckedChange = { cuentaViewModel.cambiarRedondeo(it) }
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
                    Text("Propina (${uiState.porcentajeAplicar.toInt()} %)", color = Color.Gray, fontSize = 12.sp)
                    Text(formatoMoneda(uiState.montoPropina), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total con propina", color = Color.Gray, fontSize = 12.sp)
                    Text(formatoMoneda(uiState.totalConPropina), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))

                // Gran total al centro
                Text(
                    text = "Cada quien paga",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = formatoMoneda(uiState.montoPersonaFinal),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6750A4),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "entre ${uiState.numeroPersonas} personas",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                if (uiState.redondearArriba) {
                    Text(
                        text = "Sobrante por redondeo: ${formatoMoneda(uiState.sobranteRedondeo)}",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}