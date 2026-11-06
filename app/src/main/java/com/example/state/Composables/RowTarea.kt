package com.example.state.Composables

import android.R
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.state.Clases.Tarea

@Composable
fun RowTarea(tarea: Tarea, eliminar: () -> Unit) {

    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(6.dp)){
            Checkbox(
                checked = tarea.completada,
                onCheckedChange = { tarea.completada = it }
            )

            Column(modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = tarea.Nombre,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 2.dp)
                )

                Text(
                    text = tarea.Prioridad,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (tarea.Prioridad == "Alta") Color.Red else {if (tarea.Prioridad == "Media") Color.Yellow else Color.Green },
                    modifier = Modifier.padding(1.dp).border(
                        width = 1.dp,
                        color = if (tarea.Prioridad == "Alta") Color.Red else {if (tarea.Prioridad == "Media") Color.Yellow else Color.Green } )
                )
            }
        }

        OutlinedButton(
            onClick = { eliminar },
            modifier = Modifier
                .height(50.dp)
        ) {
            Text("X")
        }


    }

    Divider(
        modifier = Modifier.padding(top = 16.dp),
        thickness = 1.dp,
        color = Color.LightGray.copy(alpha = 0.5f)
    )

}