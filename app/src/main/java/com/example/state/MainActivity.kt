package com.example.state

import android.R.attr.enabled
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
import androidx.compose.ui.text.input.KeyboardType
import com.example.state.ui.theme.StateTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.state.Clases.Producto
import com.example.state.Composables.RowProducto

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StateTheme {
                Scaffold (modifier = Modifier.fillMaxSize()) { innerPadding ->

                    InicioScreen(modifier = Modifier.padding(innerPadding).padding(horizontal = 18.dp))
                }
            }
        }
    }
}




@Composable fun InicioScreen(modifier: Modifier) {
    var C_Tortilla by remember { mutableStateOf(0) }
    var C_Leche by remember { mutableStateOf(0) }
    var C_Huevo by remember { mutableStateOf(0) }
    var C_Frijol by remember { mutableStateOf(0) }
    val Tortilla = Producto("Tortilla 1 kg", 24.00)
    val Leche = Producto("Leche 1 L", 27.50)
    val Huevo = Producto("Huevo 12 pz", 52.00)
    val Frijol = Producto("Frijol 1 kg", 38.00)
    val total = (C_Tortilla * Tortilla.precio) + (C_Leche * Leche.precio) + (C_Huevo * Huevo.precio) + (C_Frijol * Frijol.precio)


    Column (modifier.fillMaxSize().background(color = Color.White)) {

        //Primera linea
        Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            Text(text = "Tienda Don Pepe",
                fontSize = 20.sp,
                color = Color.Black)

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
                    .background(color = Color(0xFF381E72))
            ) {
                Text(
                    text = "${C_Tortilla + C_Leche + C_Huevo + C_Frijol}",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.fillMaxWidth().height(6.dp))

        // Seccion de Productos
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {

            RowProducto(Tortilla,
                {if ( C_Tortilla > 0) {C_Tortilla--} },
                {if ( C_Tortilla < 10) {C_Tortilla++} },
                C_Tortilla)

            Spacer(modifier = Modifier.fillMaxWidth().height(6.dp))

            RowProducto(Leche,
                {if ( C_Leche > 0) {C_Leche--} },
                {if ( C_Leche < 10) {C_Leche++} },
                C_Leche)

            Spacer(modifier = Modifier.fillMaxWidth().height(6.dp))

            RowProducto(Huevo,
                {if ( C_Huevo > 0) {C_Huevo--} },
                {if ( C_Huevo < 10) {C_Huevo++} },
                C_Huevo)

            Spacer(modifier = Modifier.fillMaxWidth().height(6.dp))

            RowProducto(Frijol,
                {if ( C_Frijol > 0) {C_Frijol--} },
                {if ( C_Frijol < 10) {C_Frijol++} },
                C_Frijol)
        }


        // Seccion de totales

        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp).border(width = 1.dp, color = Color.Black) )
        {
            Column(Modifier.fillMaxWidth().padding(5.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                    Text(
                        text = "Articulos",
                        fontSize = 18.sp,
                        color = Color.Black,
                    )

                    Text(
                        text = "${C_Tortilla + C_Leche + C_Huevo + C_Frijol}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                }

                Spacer(Modifier.fillMaxWidth().height(6.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                    Text(
                        text = "Subtotal",
                        fontSize = 18.sp,
                        color = Color.Black,
                    )

                    Text(
                        text = "$${(C_Tortilla * Tortilla.precio) + (C_Leche * Leche.precio) + (C_Huevo * Huevo.precio) + (C_Frijol * Frijol.precio)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                }

                Spacer(Modifier.fillMaxWidth().height(6.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                    Text(
                        text = "Descuento 10%",
                        fontSize = 18.sp,
                        color = Color.Black,
                    )

                    Text(
                        text = if (total >= 200) "-$${total * .1}" else "No aplica" ,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (total >= 200) Color.Green else Color.Black,
                    )
                }

                Spacer(Modifier.fillMaxWidth().height(6.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                    Text(
                        text = "Envio",
                        fontSize = 18.sp,
                        color = Color.Black,
                    )

                    Text(
                        text = if (C_Tortilla != 0 || C_Leche != 0 || C_Huevo != 0 || C_Frijol != 0) { if(total >= 150) "GRATIS" else "$25.0"} else "$0.0",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (C_Tortilla == 0 || C_Leche == 0 || C_Huevo == 0 || C_Frijol == 0) { if(total >= 150) Color.Green else Color.Black} else Color.Black,
                    )
                }

                Spacer(Modifier.fillMaxWidth().height(3.dp))

                Divider(
                    modifier = Modifier.padding(top = 16.dp),
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )

                Spacer(Modifier.fillMaxWidth().height(3.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                    Text(
                        text = "TOTAL",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )

                    Text(
                        text = "$${total - (total * .1)}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF381E72),
                    )
                }
            }

        }

        // Seccion Botones

        Row(modifier = Modifier.fillMaxWidth().padding(6.dp) ) {
            OutlinedButton(
                onClick = {
                    C_Tortilla = 0
                    C_Leche = 0
                    C_Huevo = 0
                    C_Frijol = 0  },
                enabled = total > 0.0,
                modifier = Modifier
                    .height(50.dp)
            ) {
                Text("Vaciar")
            }

            Spacer(Modifier.width(12.dp))

            Button(
                onClick = {
                    C_Tortilla = 0
                    C_Leche = 0
                    C_Huevo = 0
                    C_Frijol = 0  },
                enabled = total > 0.0,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF381E72)),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
            ) {
                Text("Confirmar compra")
            }

        }


    }

}






@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    InicioScreen(modifier = Modifier)
}