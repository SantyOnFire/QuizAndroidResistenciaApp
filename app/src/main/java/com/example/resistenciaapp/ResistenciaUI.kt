package com.example.resistenciaapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.pow

@Composable
fun CalculadoraResistenciaUI() {
    val colores = listOf("Negro", "Marrón", "Rojo", "Naranja", "Amarillo", "Verde", "Azul", "Violeta", "Gris", "Blanco")
    val multiplicadores = colores + listOf("Dorado", "Plateado")
    val tolerancias = mapOf(
        "Marrón" to "±1%", "Rojo" to "±2%", "Verde" to "±0.5%", "Azul" to "±0.25%",
        "Violeta" to "±0.1%", "Gris" to "±0.05%", "Dorado" to "±5%", "Plateado" to "±10%", "Ninguno" to "±20%"
    )

    var banda1 by remember { mutableStateOf(colores[0]) }
    var banda2 by remember { mutableStateOf(colores[0]) }
    var multiplicador by remember { mutableStateOf(multiplicadores[0]) }
    var tolerancia by remember { mutableStateOf(tolerancias.keys.first()) }
    var resultado by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🎨 Calculadora de Resistencias",
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(20.dp)
            ) {
                ColorDropdown("Banda 1", colores, banda1) { banda1 = it }
                ColorDropdown("Banda 2", colores, banda2) { banda2 = it }
                ColorDropdown("Multiplicador", multiplicadores, multiplicador) { multiplicador = it }
                ColorDropdown("Tolerancia", tolerancias.keys.toList(), tolerancia) { tolerancia = it }
            }
        }

        Button(
            onClick = {
                val valor1 = colores.indexOf(banda1)
                val valor2 = colores.indexOf(banda2)
                val mult = when (multiplicador) {
                    "Dorado" -> 0.1
                    "Plateado" -> 0.01
                    else -> 10.0.pow(colores.indexOf(multiplicador))
                }
                val resistencia = ((valor1 * 10) + valor2) * mult
                resultado = "$resistencia Ω ${tolerancias[tolerancia]}"
            },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Calcular Resistencia", fontSize = 17.sp, fontWeight = FontWeight.Medium)
        }

        if (resultado.isNotEmpty()) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(14.dp),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = "Resultado: $resultado",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(18.dp)
                )
            }
        }
    }
}
//permite utilizar funciones o componentes de la biblioteca Material 3
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorDropdown(
    label: String,
    opciones: List<String>,
    seleccion: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = seleccion,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .background(getColorFromName(opcion), shape = CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(opcion)
                        }
                    },
                    onClick = {
                        onSelected(opcion)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun getColorFromName(nombre: String): Color {
    return when (nombre) {
        "Negro" -> Color.Black
        "Marrón" -> Color(0xFF8B4513)
        "Rojo" -> Color.Red
        "Naranja" -> Color(0xFFFFA500)
        "Amarillo" -> Color.Yellow
        "Verde" -> Color.Green
        "Azul" -> Color.Blue
        "Violeta" -> Color(0xFF8A2BE2)
        "Gris" -> Color.Gray
        "Blanco" -> Color.White
        "Dorado" -> Color(0xFFFFD700)
        "Plateado" -> Color(0xFFC0C0C0)
        "Ninguno" -> Color.LightGray
        else -> Color.Transparent
    }
}
