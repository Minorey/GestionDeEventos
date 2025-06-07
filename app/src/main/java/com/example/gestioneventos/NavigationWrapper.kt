package com.example.gestioneventos

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gestioneventos.presentation.events.EventForm
import com.example.gestioneventos.presentation.events.EventsScreen
import com.example.gestioneventos.presentation.events.EventsViewModel
import com.example.gestioneventos.presentation.login.AuthViewModel
import com.example.gestioneventos.presentation.login.LoginScreen
import com.example.gestioneventos.presentation.main.MainScreen
import com.example.gestioneventos.presentation.register.RegisterScreen

@Composable
fun NavigationWrapper(
    navHostController: NavHostController,
    viewModel: AuthViewModel,
    modifier: Modifier,
    viewModelEventsViewModel: EventsViewModel
) {
    NavHost(navController = navHostController, startDestination = "initial") {
        composable("initial") {
            MainScreen(
                modifier = modifier,
                navController = navHostController,
                viewModel = viewModel,
                navigateToLogin = { navHostController.navigate("login") },
                navigateToSignUp = { navHostController.navigate("signUp") })
        }
        composable("login") {
            LoginScreen(
                modifier = modifier,
                viewModel = viewModel,
                onLoginSuccess = { navHostController.navigate("events") },
                onNavigateToRegister = { navHostController.navigate("register") }
            )
        }
        composable("signUp") {
            RegisterScreen(
                modifier = modifier,
                viewModel = viewModel,
                onRegisterSuccess = { navHostController.navigate("events") },
                onNavigateToLogin = { navHostController.popBackStack("login", inclusive = false) }
            )
        }
        composable("events") {
            EventsScreen(modifier = modifier,viewModel = viewModelEventsViewModel)
        }
        composable("create") {
            EventForm(viewModel = viewModelEventsViewModel)
            { navHostController.popBackStack() } }
    }
}