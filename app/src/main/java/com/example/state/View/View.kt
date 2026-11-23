package com.example.state.View

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.state.Model.Gasto
import com.example.state.ViewModel.PresupuestoViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    presupuestoViewModel: PresupuestoViewModel = viewModel() // 1. Conectamos el ViewModel
) {
    // 2. Observamos el estado global expuesto por el ViewModel
    val uiState by presupuestoViewModel.uiState.collectAsState()

    // Colores dinámicos para la barra de progreso según el porcentaje usado
    val colorIndicador = when {
        uiState.presupuestoExcedido -> Color(0xFFD32F2F) // Rojo (Excedido >= 100%)
        uiState.mostrarAlerta80 -> Color(0xFFF57F17)     // Amarillo alerta (>= 80%)
        else -> Color(0xFFFBC02D)                        // Amarillo estándar
    }

    // Formateador de moneda puramente visual
    val formatoMoneda = NumberFormat.getCurrencyInstance(Locale.US)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Mi presupuesto", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        // Campo Editable de Ingreso
        OutlinedTextField(
            value = uiState.ingresoText,
            onValueChange = { presupuestoViewModel.actualizarIngreso(it) },
            label = { Text("Ingreso mensual") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = { Text("$") },
            modifier = Modifier.fillMaxWidth()
        )

        // Resumen Financiero
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E6)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Gastado", fontSize = 12.sp, color = Color.Gray)
                        Text(
                            text = formatoMoneda.format(uiState.totalGastado),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Saldo", fontSize = 12.sp, color = Color.Gray)
                        Text(
                            text = formatoMoneda.format(uiState.saldoDisponible),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (uiState.saldoDisponible < 0) Color.Red else Color(0xFF2E7D32)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = { uiState.porcentajeUsado.toFloat().coerceIn(0f, 1f) },
                    color = colorIndicador,
                    trackColor = Color(0xFFE0E0E0),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

                Spacer(modifier = Modifier.height(12.dp))

                val textoCategoria = uiState.categoriaMayorGasto?.let {
                    "Mayor gasto: ${it.key} (${formatoMoneda.format(it.value)})"
                } ?: "Aún no hay gastos"

                Text(
                    text = "${uiState.porcentajeTexto} % usado · $textoCategoria",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }

        // Alertas Dinámicas (>= 80% y >= 100%)
        if (uiState.mostrarAlerta80) {
            val mensajeAlerta = if (uiState.presupuestoExcedido) {
                "¡Peligro! Te excediste por ${formatoMoneda.format(uiState.montoExcedido)}"
            } else {
                "Cuidado: pasaste el 80 %"
            }

            Surface(
                color = if (uiState.presupuestoExcedido) Color(0xFFFFCDD2) else Color(0xFFFFE0B2),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "⚠ $mensajeAlerta",
                    color = if (uiState.presupuestoExcedido) Color(0xFFC62828) else Color(0xFFE65100),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // 3. Formulario de Nuevo Gasto (Componente Aislado con Estado Local)
        Text("Nuevo gasto", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
        FormularioGasto(
            categorias = uiState.categoriasDisponibles,
            onAgregar = { nuevoGasto ->
                presupuestoViewModel.agregarGasto(nuevoGasto)
            }
        )

        // 4. Lista de Gastos Recientes
        Text("Gastos recientes", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
        if (uiState.listaGastos.isEmpty()) {
            Text("No hay gastos registrados", color = Color.Gray, fontSize = 14.sp)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                uiState.listaGastos.forEach { gasto ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(gasto.concepto, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(gasto.categoria, color = Color.Gray, fontSize = 12.sp)
                        }
                        Text(
                            text = "-${formatoMoneda.format(gasto.monto)}",
                            color = Color(0xFFD32F2F),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Eliminar",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { presupuestoViewModel.eliminarGasto(gasto.id) }
                        )
                    }
                    HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.5f))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioGasto(
    categorias: List<String>,
    onAgregar: (Gasto) -> Unit
) {
    // ESTADO LOCAL: No sube al ViewModel porque solo es edición temporal
    var concepto by remember { mutableStateOf("") }
    var montoText by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf(categorias.firstOrNull() ?: "Comida") }
    var dropdownExpandido by remember { mutableStateOf(false) }

    val montoNumerico = montoText.toDoubleOrNull()
    val isFormularioValido = concepto.isNotBlank() && montoNumerico != null && montoNumerico > 0

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = concepto,
            onValueChange = { concepto = it },
            label = { Text("Concepto") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = montoText,
                onValueChange = { montoText = it },
                label = { Text("Monto") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.weight(1f)
            )

            ExposedDropdownMenuBox(
                expanded = dropdownExpandido,
                onExpandedChange = { dropdownExpandido = !dropdownExpandido },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpandido) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = dropdownExpandido,
                    onDismissRequest = { dropdownExpandido = false }
                ) {
                    categorias.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                categoria = opcion
                                dropdownExpandido = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (isFormularioValido) {
                    // 1. Entregamos el gasto terminado al ViewModel
                    onAgregar(Gasto(concepto = concepto.trim(), monto = montoNumerico!!, categoria = categoria))
                    // 2. Limpiamos el formulario local
                    concepto = ""
                    montoText = ""
                    categoria = categorias.firstOrNull() ?: "Comida"
                }
            },
            enabled = isFormularioValido,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8DEF8)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Agregar gasto", color = Color(0xFF6750A4), fontWeight = FontWeight.Bold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    InicioScreen()
}