package com.example.laboratorio6

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.laboratorio6.ui.theme.Laboratorio6Theme
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Laboratorio6Theme {
                PantallaContador(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
data class Movimiento(val valor: Int, val esIncremento: Boolean)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PantallaContador(
    modifier: Modifier = Modifier,
    nombre: String = "Mariana Castañeda Ramirez",
) {

    var contador by remember { mutableIntStateOf(0) }
    var incrementos by remember { mutableIntStateOf(0) }
    var decrementos by remember { mutableIntStateOf(0) }
    var maximo by remember { mutableIntStateOf(0) }
    var minimo by remember { mutableIntStateOf(0) }
    var cambios by remember { mutableIntStateOf(0) }
    val historial = remember { mutableStateListOf<Movimiento>() }
    val altoHistorial = 212.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),

    ) {

        Text(
            text = nombre,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp),


        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            FilledIconButton(
                onClick = {
                    contador--
                    decrementos++
                    cambios++
                    maximo = maxOf(maximo, contador)
                    minimo = if (cambios == 1) contador else minOf(minimo, contador)
                    historial.add(Movimiento(contador, false))
                },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Menos")
            }

            Text(
                text = contador.toString(),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            FilledIconButton(
                onClick = {
                    contador++
                    incrementos++
                    cambios++
                    maximo = maxOf(maximo, contador)
                    minimo = if (cambios == 1) contador else minOf(minimo, contador)
                    historial.add(Movimiento(contador, true))
                },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Más")
            }
        }

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
            thickness = 1.dp
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilaEstadistica("Total incrementos:", incrementos)
            FilaEstadistica("Total decrementos:", decrementos)
            FilaEstadistica("Valor máximo:", maximo)
            FilaEstadistica("Valor mínimo:", minimo)
            FilaEstadistica("Total cambios:", cambios)

            Text(
                text = "Historial:",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(altoHistorial),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(historial.chunked(5)) { fila ->
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachRow = 5
                    ) {
                        fila.forEach { mov ->
                            ChipHistorial(
                                texto = mov.valor.toString(),
                                colorFondo = if (mov.esIncremento) Color(0xFF2E7D32) else Color(0xFFC62828)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp)) // separa la lista del botón

        Button(
            onClick = {
                contador = 0
                incrementos = 0
                decrementos = 0
                maximo = 0
                minimo = 0
                cambios = 0
                historial.clear()
            },
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Reiniciar")
        }
    }
}

@Composable
private fun FilaEstadistica(
    titulo: String,
    valor: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Text(text = valor.toString(), style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun ChipHistorial(
    texto: String,
    colorFondo: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(width = 56.dp, height = 36.dp)
            .background(colorFondo, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(texto, color = Color.White, fontWeight = FontWeight.Bold)
    }
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
private fun PreviaPantallaContador() {
    Laboratorio6Theme {
        PantallaContador(
            modifier = Modifier.fillMaxSize()
        )
    }
}

