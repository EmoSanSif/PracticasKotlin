package com.example.state

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.state.ui.theme.StateTheme
import java.text.NumberFormat
import java.util.Locale

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

data class Gasto(
    val concepto: String,
    val monto: Double,
    val categoria: String
)

@Composable
fun InicioScreen(modifier: Modifier = Modifier) {
    // 1. Definición del Modelo de Datos


        // 2. Estado Global (Compartido)
        var ingresoText by remember { mutableStateOf("4500") }
        val ingresoMensual = ingresoText.toDoubleOrNull() ?: 0.0
        val listaGastos = remember { mutableStateListOf<Gasto>() }

        // 3. Cálculos Derivados (Se actualizan automáticamente)
        val totalGastado = listaGastos.sumOf { it.monto }
        val saldoDisponible = ingresoMensual - totalGastado

        val porcentajeUsado = if (ingresoMensual > 0) (totalGastado / ingresoMensual) else 0.0
        val porcentajeTexto = (porcentajeUsado * 100).toInt()

        // Búsqueda de la categoría con mayor gasto
        val gastoPorCategoria = listaGastos.groupBy { it.categoria }.mapValues { entry -> entry.value.sumOf { it.monto } }
        val categoriaMayorGasto = gastoPorCategoria.maxByOrNull { it.value }

        // Colores dinámicos para el estado del presupuesto
        val colorIndicador = when {
            porcentajeTexto >= 100 -> Color(0xFFD32F2F) // Rojo (Excedido)
            porcentajeTexto >= 80 -> Color(0xFFF57F17) // Amarillo (Peligro)
            else -> Color(0xFFFBC02D) // Amarillo estándar de la barra de progreso
        }

        // Formateador de moneda
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
                value = ingresoText,
                onValueChange = { ingresoText = it },
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
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Gastado", fontSize = 12.sp, color = Color.Gray)
                            Text(formatoMoneda.format(totalGastado), fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Saldo", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                formatoMoneda.format(saldoDisponible),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (saldoDisponible < 0) Color.Red else Color(0xFF2E7D32)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LinearProgressIndicator(
                        progress = porcentajeUsado.toFloat().coerceIn(0f, 1f),
                        color = colorIndicador,
                        trackColor = Color(0xFFE0E0E0),
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val textoCategoria = categoriaMayorGasto?.let {
                        "Mayor gasto: ${it.key} (${formatoMoneda.format(it.value)})"
                    } ?: "Aún no hay gastos"

                    Text(
                        text = "$porcentajeTexto % usado · $textoCategoria",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }

            // Alertas Dinamicas
            if (porcentajeTexto >= 80) {
                val mensajeAlerta = if (porcentajeTexto >= 100) {
                    "¡Peligro! Te excediste por ${formatoMoneda.format(totalGastado - ingresoMensual)}"
                } else {
                    "Cuidado: pasaste el 80 %"
                }

                Surface(
                    color = if (porcentajeTexto >= 100) Color(0xFFFFCDD2) else Color(0xFFFFE0B2),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚠ $mensajeAlerta",
                        color = if (porcentajeTexto >= 100) Color(0xFFC62828) else Color(0xFFE65100),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // 4. Formulario de Nuevo Gasto (Componente Aislado)
            Text("Nuevo gasto", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
            FormularioGasto(onAgregar = { nuevoGasto ->
                listaGastos.add(nuevoGasto)
            })

            // 5. Lista de Gastos Recientes
            Text("Gastos recientes", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
            if (listaGastos.isEmpty()) {
                Text("No hay gastos registrados", color = Color.Gray, fontSize = 14.sp)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listaGastos.forEach { gasto ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
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
                                    .size(16.dp)
                                    .clickable { listaGastos.remove(gasto) }
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FormularioGasto(onAgregar: (Gasto) -> Unit) {
        // ESTADO LOCAL: Esto no debe subirse (hoistearse) al componente padre
        var concepto by remember { mutableStateOf("") }
        var montoText by remember { mutableStateOf("") }
        var categoria by remember { mutableStateOf("Comida") }
        var dropdownExpandido by remember { mutableStateOf(false) }

        val categorias = listOf("Comida", "Transporte", "Renta", "Ocio", "Otros")
        val montoNumerico = montoText.toDoubleOrNull()
        val isFormularioValido = concepto.isNotBlank() && montoNumerico != null && montoNumerico > 0

        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = concepto,
                onValueChange = { concepto = it },
                label = { Text("Concepto") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = montoText,
                    onValueChange = { montoText = it },
                    label = { Text("Monto") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                        modifier = Modifier.menuAnchor().fillMaxWidth()
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
                        // 1. Enviamos el gasto terminado al padre
                        onAgregar(Gasto(concepto, montoNumerico!!, categoria))
                        // 2. Limpiamos el formulario local
                        concepto = ""
                        montoText = ""
                        categoria = "Comida"
                    }
                },
                enabled = isFormularioValido,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE8DEF8)),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Agregar gasto", color = Color(0xFF6750A4), fontWeight = FontWeight.Bold)
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