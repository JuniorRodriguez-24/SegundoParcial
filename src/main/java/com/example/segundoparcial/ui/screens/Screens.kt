package com.example.segundoparcial.ui.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import androidx.navigation.NavController
import com.example.segundoparcial.database.DatabaseHelper
import com.example.segundoparcial.model.Cita

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun PantallaDatosPersonales(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    var errorNombre by remember { mutableStateOf(false) }
    var errorTelefono by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Datos del Paciente", color = Color.White) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Nueva Cita Médica", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    errorNombre = it.isBlank()
                },
                label = { Text("Nombre Completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                isError = errorNombre,
                supportingText = { if (errorNombre) Text("El nombre no puede estar vacío", color = MaterialTheme.colorScheme.error) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = telefono,
                onValueChange = { input ->
                    if (input.all { it.isDigit() } && input.length <= 10) {
                        telefono = input
                        errorTelefono = input.length != 10
                    }
                },
                label = { Text("Número de Teléfono (10 dígitos)") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = errorTelefono,
                supportingText = { if (errorTelefono) Text("El teléfono debe tener exactamente 10 dígitos", color = MaterialTheme.colorScheme.error) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val isNombreValid = nombre.isNotBlank()
                    val isTelefonoValid = telefono.length == 10

                    errorNombre = !isNombreValid
                    errorTelefono = !isTelefonoValid

                    // Si las validaciones pasan, navega a la pantalla 2 enviando los datos
                    if (isNombreValid && isTelefonoValid) {
                        navController.navigate("pantalla2/$nombre/$telefono")
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
               Text("Continuar", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { navController.navigate("pantalla3") },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.List, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver todas las citas", fontSize = 16.sp)
            }
        }
    }
}
@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFechaHora(navController: NavController, dbHelper: DatabaseHelper, nombre: String, telefono: String) {
    val context = LocalContext.current
    val calendario = Calendar.getInstance()

    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, año, mes, dia -> fecha = "$dia/${mes + 1}/$año" },
        calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, h, m -> hora = String.format("%02d:%02d", h, m) },
        calendario.get(Calendar.HOUR_OF_DAY), calendario.get(Calendar.MINUTE), true
    )

    Scaffold(
        topBar = { TopAppBar(title = {Text("Agendar Horario", color = Color.White) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Paciente: $nombre", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Teléfono : $telefono", color = Color.Gray, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Selector de fecha
            OutlinedButton(
                onClick = { datePickerDialog.show() },
                modifier = Modifier.fillMaxWidth().height(55.dp)
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text( if(fecha.isEmpty()) "Seleccionar Fecha" else "Fecha: $fecha", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de Hora
            OutlinedButton(
                onClick = {timePickerDialog.show()},
                modifier = Modifier.fillMaxWidth().height(55.dp)
            ) {
                Text(if (hora.isEmpty()) "Seleccionar Hora" else "Hora: $hora", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (fecha.isEmpty() || hora.isEmpty()) {
                        Toast.makeText(context, "Por favor seleccione fecha y hora de la cita", Toast.LENGTH_SHORT).show()
                    } else {
                        val nuevaCita = Cita(nombre = nombre, telefono = telefono, fecha = fecha, hora = hora)
                        val idGenerado = dbHelper.insertarCita(nuevaCita)
                        if (idGenerado > 0) {
                            Toast.makeText(context, "Cita agendada con éxito", Toast.LENGTH_SHORT).show()
                            navController.navigate("pantalla4/$idGenerado") {
                                popUpTo("pantalla1") { inclusive = false }
                            }
                        } else {
                            Toast.makeText(context, "Error interno al guardar en SQLite", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Confirmar Cita", fontSize = 16.sp)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListaCita(navController: NavController, dbHelper: DatabaseHelper) {
    var listaCitas by remember { mutableStateOf(listOf<Cita>()) }
    var context = LocalContext.current

    // Carga los datos desde SQLite al entrar en la pantalla
    LaunchedEffect(Unit) {
        listaCitas = dbHelper.obtenerCitas()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Citas Agendadas", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { paddingValues ->
        if (listaCitas.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No hay citas registradas en el sistema.",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listaCitas) { cita ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    cita.nombre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    "Tel: ${cita.telefono}",
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row {
                                    Text(
                                        "Fecha: ${cita.fecha}  |  ",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        "Hora: ${cita.hora}",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Row {
                                // Botón de Edición
                                IconButton(onClick = { navController.navigate("pantalla_editar/${cita.id}") }) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Editar Cita",
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                }
                                // Botón de Eliminación
                                IconButton(onClick = {
                                    val filasEliminadas = dbHelper.eliminarCita(cita.id)
                                    if (filasEliminadas > 0) {
                                        Toast.makeText(
                                            context,
                                            "Cita eliminada correctamente",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        listaCitas = dbHelper.obtenerCitas()
                                    }
                                }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Eliminar Cita",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaResumenCita(navController: NavController, dbHelper: DatabaseHelper, citaId: Int) {
    var appointment by remember { mutableStateOf<Cita?>(null) }

    LaunchedEffect(citaId) {
        appointment = dbHelper.obtenerCitaPorId(citaId)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Resumen de la Cita", color = Color.White) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("¡Confirmación Exitosa!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(24.dp))

            appointment?.let {cita ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape (16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Detalles Generales", fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                        Text("Nombre del Paciente:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(cita.nombre, fontSize = 18.sp, modifier = Modifier.padding(bottom = 12.dp))

                        Text("Teléfono de Contacto:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(cita.telefono, fontSize = 18.sp, modifier = Modifier.padding(bottom = 12.dp))

                        Text("Fecha Programada:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(cita.fecha, fontSize = 18.sp, modifier = Modifier.padding(bottom = 12.dp))

                        Text("Hora Programada:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(cita.hora, fontSize = 18.sp)
                    }
                }
            } ?: Text("Cargando información...")

            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = { navController.navigate("pantalla1") { popUpTo("pantalla1") { inclusive = true } } }, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                Text("Regresar al Inicio")
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarCita(navController: NavController, dbHelper: DatabaseHelper, citaId: Int) {
    val context = LocalContext.current
    val calendario = Calendar.getInstance()

    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }

    LaunchedEffect(citaId) {
        dbHelper.obtenerCitaPorId(citaId)?.let {
            nombre = it.nombre
            telefono = it.telefono
            fecha = it.fecha
            hora = it.hora
        }
    }

    val datePickerDialog = DatePickerDialog(
        context, { _, año, mes, día -> fecha = "$día/${mes + 1}/$año" },
        calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH)
    )
    val timePickerDialog = TimePickerDialog(
        context, { _, h, m -> hora = String.format("%02d:%02d", h, m) },
        calendario.get(Calendar.HOUR_OF_DAY), calendario.get(Calendar.MINUTE), true
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Modificar Cita", color = Color.White) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)) }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = { datePickerDialog.show() }, modifier = Modifier.weight(1f)) { Text(if (fecha.isEmpty()) "Fecha" else fecha) }
                OutlinedButton(onClick = { timePickerDialog.show() }, modifier = Modifier.weight(1f)) { Text(if (hora.isEmpty()) "Hora" else hora) }
            }

            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = {
                    if (nombre.isNotBlank() && telefono.length == 10) {
                        dbHelper.actualizarCita(Cita(citaId, nombre, telefono, fecha, hora))
                        Toast.makeText(context, "Cita actualizada", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Verifique los datos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) { Text("Guardar Cambios") }
        }
    }
}
