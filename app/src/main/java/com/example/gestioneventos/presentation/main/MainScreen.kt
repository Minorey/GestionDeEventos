package com.example.gestioneventos.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gestioneventos.R
import com.example.gestioneventos.presentation.login.AuthViewModel

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    navigateToLogin: () -> Unit = {},
    navigateToSignUp: () -> Unit = {},
    modifier: Modifier
) {
    val isloggedIn = viewModel.isUserLoggedIn()
    LaunchedEffect(Unit) {
        if (isloggedIn) {
            navController.navigate("events") {
                popUpTo("initial") { inclusive = true }
            }
        }
    }
    val gradientColors = listOf(Color(0xFF1DB954), Color.Magenta)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(gradientColors))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(32.dp))


            Image(
                painter = painterResource(id = R.drawable.logoenventos),
                contentDescription = "EVENTOS",
                modifier = Modifier
                    .size(96.dp)

            )


            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Controla tus Eventos.",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Con Eventos App.",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { navigateToSignUp() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(
                        text = "Sign up free",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }

                SocialButton(
                    icon = painterResource(id = R.drawable.google),
                    text = "Continua con google"
                )

                SocialButton(
                    icon = painterResource(id = R.drawable.facebook),
                    text = "Continua con Facebook"
                )

                Text(
                    text = "Inicia Sesion",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { navigateToLogin() }
                        .padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SocialButton(icon: Painter, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(CircleShape)
            .border(1.dp, Color.White, CircleShape)
            .clickable { }
            .padding(horizontal = 16.dp)
    ) {
        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}