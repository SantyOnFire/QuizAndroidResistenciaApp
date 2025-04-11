package com.example.resistenciaapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Calculadora de Resistencia", fontSize = 24.sp)
        Spacer(Modifier.height(8.dp))

        ColorDropdown("Banda 1", colores, banda1) { banda1 = it }
        ColorDropdown("Banda 2", colores, banda2) { banda2 = it }
        ColorDropdown("Multiplicador", multiplicadores, multiplicador) { multiplicador = it }
        ColorDropdown("Tolerancia", tolerancias.keys.toList(), tolerancia) { tolerancia = it }

        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            val valor1 = colores.indexOf(banda1)
            val valor2 = colores.indexOf(banda2)
            val mult = when (multiplicador) {
                "Dorado" -> 0.1
                "Plateado" -> 0.01
                else -> 10.0.pow(colores.indexOf(multiplicador))
            }
            val resistencia = ((valor1 * 10) + valor2) * mult
            resultado = "$resistencia Ω ${tolerancias[tolerancia]}"
        }) {
            Text("Calcular")
        }

        Spacer(Modifier.height(16.dp))
        Text(text = resultado, fontSize = 22.sp)
    }
}

@Composable
fun ColorDropdown(label: String, opciones: List<String>, seleccion: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(text = label)
        Box {
            OutlinedTextField(
                value = seleccion,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                label = { Text(label) }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            onSelected(opcion)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
