package com.example.state.View

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.state.ViewModel.CarritoViewModel
import java.util.Locale

@Composable
fun InicioScreen(
    modifier: Modifier = Modifier,
    carritoViewModel: CarritoViewModel = viewModel()
) {
    val uiState by carritoViewModel.uiState.collectAsState()

    fun formatoMoneda(valor: Double): String = String.format(Locale.US, "$%,.2f", valor)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tienda Don Pepe",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(color = Color(0xFF381E72))
            ) {
                Text(
                    text = "${uiState.totalArticulos}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de productos generada dinámicamente desde uiState.productos
        Column(modifier = Modifier.fillMaxWidth()) {
            uiState.productos.forEach { producto ->
                val cantidadActual = uiState.cantidadDe(producto.id)

                RowProducto(
                    producto = producto,
                    cantidad = cantidadActual,
                    onRestar = { carritoViewModel.decrementarCantidad(producto.id) },
                    onSumar = { carritoViewModel.incrementarCantidad(producto.id) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Tarjeta de resumen de totales
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F3FB)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Artículos", fontSize = 14.sp, color = Color.DarkGray)
                    Text(
                        text = "${uiState.totalArticulos}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Subtotal", fontSize = 14.sp, color = Color.DarkGray)
                    Text(
                        text = formatoMoneda(uiState.subtotal),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Descuento 10 %", fontSize = 14.sp, color = Color.DarkGray)
                    Text(
                        text = if (uiState.aplicaDescuento) "-${formatoMoneda(uiState.descuento)}" else "No aplica",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (uiState.aplicaDescuento) Color(0xFF2E7D32) else Color.Gray
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Envío", fontSize = 14.sp, color = Color.DarkGray)
                    Text(
                        text = if (uiState.envioGratis) "GRATIS" else formatoMoneda(uiState.costoEnvio),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (uiState.envioGratis) Color(0xFF2E7D32) else Color.Black
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.6f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TOTAL",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = formatoMoneda(uiState.totalFinal),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF381E72)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { carritoViewModel.vaciarCarrito() },
                enabled = !uiState.carritoVacio,
                modifier = Modifier.height(50.dp)
            ) {
                Text("Vaciar")
            }

            Button(
                onClick = { carritoViewModel.confirmarPedido() },
                enabled = !uiState.carritoVacio,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF381E72)),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text("Confirmar pedido")
            }
        }
    }
}

@Composable
fun RowProducto(
    producto: com.example.state.Model.Producto,
    cantidad: Int,
    onRestar: () -> Unit,
    onSumar: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = producto.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = String.format(Locale.US, "$%.2f c/u", producto.precio),
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                BotonCircularStepper(
                    simbolo = "−",
                    habilitado = cantidad > 0,
                    onClick = onRestar
                )

                Text(
                    text = "$cantidad",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 14.dp)
                )

                BotonCircularStepper(
                    simbolo = "+",
                    habilitado = cantidad < 10,
                    onClick = onSumar
                )
            }
        }

        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.6f))
    }
}

@Composable
private fun BotonCircularStepper(
    simbolo: String,
    habilitado: Boolean,
    onClick: () -> Unit
) {
    val colorBorde = if (habilitado) Color.Gray else Color.LightGray.copy(alpha = 0.5f)
    val colorTexto = if (habilitado) Color.Black else Color.LightGray

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .border(1.dp, colorBorde, CircleShape)
            .clickable(enabled = habilitado) { onClick() }
    ) {
        Text(text = simbolo, fontSize = 18.sp, color = colorTexto)
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    InicioScreen()
}