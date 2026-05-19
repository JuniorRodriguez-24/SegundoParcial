package com.example.segundoparcial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.segundoparcial.database.DatabaseHelper
import com.example.segundoparcial.ui.screens.PantallaDatosPersonales
import com.example.segundoparcial.ui.screens.PantallaEditarCita
import com.example.segundoparcial.ui.screens.PantallaFechaHora
import com.example.segundoparcial.ui.screens.PantallaListaCita
import com.example.segundoparcial.ui.screens.PantallaResumenCita

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

// Es mejor práctica tener las funciones Composable fuera de la clase MainActivity
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val dbHelper = DatabaseHelper(context)

    NavHost(navController = navController, startDestination = "pantalla1") {

        // Pantalla 1: Ingreso de Datos
        composable("pantalla1") {
            PantallaDatosPersonales(navController)
        }

        // Pantalla 2: Seleccionar Fecha y Hora
        composable(
            route = "pantalla2/{nombre}/{telefono}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("telefono") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val telefono = backStackEntry.arguments?.getString("telefono") ?: ""
            PantallaFechaHora(navController, dbHelper, nombre, telefono)
        }

        // Pantalla 3: Lista general de citas
        composable("pantalla3") {
            PantallaListaCita(navController, dbHelper)
        }

        // Pantalla 4: Resumen tras confirmar
        composable(
            route = "pantalla4/{citaId}",
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
            PantallaResumenCita(navController, dbHelper, citaId)
        }

        // PANTALLA EXTRA: Requerida para que no crashee al hacer clic en "Editar" en la Lista
        composable(
            route = "pantalla_editar/{citaId}",
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
            PantallaEditarCita(navController, dbHelper, citaId)
        }
    }
}