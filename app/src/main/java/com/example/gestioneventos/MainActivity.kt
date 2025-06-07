package com.example.gestioneventos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gestioneventos.presentation.events.EventsViewModel
import com.example.gestioneventos.presentation.login.AuthViewModel
import com.example.gestioneventos.ui.theme.GestionEventosTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navHostController: NavHostController

    // private lateinit var auth: FirebaseAuth
    private val loginViewModel: AuthViewModel by viewModels()
    private val eventsViewModel: EventsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //   auth = Firebase.auth
        enableEdgeToEdge()
        setContent {
            GestionEventosTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationWrapper(
                        navHostController = navController,
                        viewModel = loginViewModel,
                        modifier = Modifier.padding(innerPadding),
                        viewModelEventsViewModel = eventsViewModel)
                    //ErrorCrashLitys("si", modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ErrorCrashLitys(text: String, modifier: Modifier) {
    Text(
        text = "MI ERROR CRASH LITYS${text}",
        modifier = modifier.clickable {
            throw RuntimeException("ESTO ES UN FALLO")
        }
    )

}
