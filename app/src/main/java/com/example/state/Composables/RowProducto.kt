package com.example.state.Composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.state.Clases.Producto

@Composable
fun RowProducto( producto: Producto, BotonR: () -> Unit, BotonM: () -> Unit, cantidad: Int) {
    Row(modifier = Modifier.fillMaxWidth()
        .border(width = 1.dp, color = Color.Black, shape = RectangleShape),
        horizontalArrangement = Arrangement.SpaceBetween )
    {
        Column(horizontalAlignment = Alignment.CenterHorizontally){

            Text(text = producto.nombre,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black)

            Text(text = "$${producto.precio} c/u",
                fontSize = 14.sp,
                color = Color.Gray)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {

            BotonCircular("-", BotonR)

            Spacer(Modifier.width(10.dp))

            Text(text = "$cantidad",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black)

            Spacer(Modifier.width(10.dp))

            BotonCircular("+", BotonM)

        }

    }

    Divider(
        modifier = Modifier.padding(top = 16.dp),
        thickness = 1.dp,
        color = Color.LightGray.copy(alpha = 0.5f)
    )
}