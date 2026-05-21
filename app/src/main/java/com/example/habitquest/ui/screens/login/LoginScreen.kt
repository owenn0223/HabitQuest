package com.example.habitquest.ui.screens.login

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.habitquest.ui.screens.login.EstadoLogin
import com.example.habitquest.ui.screens.login.LoginViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onBackClick: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    onCreateCharacterClick: () -> Unit = {}
) {
    val showPassword = remember { mutableStateOf(false) }

    // Observar estados del ViewModel
    val estadoLogin = viewModel.estadoLogin.collectAsState()
    val correo = viewModel.correo.collectAsState()
    val contraseña = viewModel.contraseña.collectAsState()

    // Efecto para manejar navegación después de login exitoso
    LaunchedEffect(estadoLogin.value) {
        if (estadoLogin.value is EstadoLogin.Exitoso) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a3a2a))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // HEADER CON BOTÓN ATRÁS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "←",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Log In",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // LOGO PEQUEÑO
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(
                        width = 3.dp,
                        color = Color(0xFF00FF88),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(
                        color = Color(0xFF0d6b4f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✔✔",
                    fontSize = 50.sp,
                    color = Color(0xFF00FF88),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // TÍTULO
        Text(
            text = "Welcome Back",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // SUBTÍTULO
        Text(
            text = "Log in to continue your quest and track your\ndaily habits.",
            fontSize = 14.sp,
            color = Color(0xFFAAAAAA),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // EMAIL ADDRESS LABEL
        Text(
            text = "Email Address",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // EMAIL ADDRESS INPUT
        OutlinedTextField(
            value = correo.value,
            onValueChange = { viewModel.actualizarCorreo(it) },
            placeholder = {
                Text(
                    text = "hero@habitquest.com",
                    color = Color(0xFF666666),
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✉️",
                        fontSize = 18.sp
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00FF88),
                unfocusedBorderColor = Color(0xFF004D40),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color(0xFFCCCCCC)
            ),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // PASSWORD LABEL
        Text(
            text = "Password",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // PASSWORD INPUT
        OutlinedTextField(
            value = contraseña.value,
            onValueChange = { viewModel.actualizarContraseña(it) },
            placeholder = {
                Text(
                    text = "••••••••",
                    color = Color(0xFF666666),
                    fontSize = 14.sp
                )
            },
            visualTransformation = if (showPassword.value) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            leadingIcon = {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🔒",
                        fontSize = 18.sp
                    )
                }
            },
            trailingIcon = {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { showPassword.value = !showPassword.value },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (showPassword.value) "👁️" else "🚫",
                        fontSize = 18.sp
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00FF88),
                unfocusedBorderColor = Color(0xFF004D40),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color(0xFFCCCCCC)
            ),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // FORGOT PASSWORD LINK
        Text(
            text = "Forgot Password?",
            fontSize = 14.sp,
            color = Color(0xFF00FF88),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { /* TODO: Navegar a recuperar contraseña */ }
        )

        Spacer(modifier = Modifier.height(28.dp))

        // BOTÓN LOG IN
        Button(
            onClick = { viewModel.iniciarSesion() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00FF88)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Log In →",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1a3a2a)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // LINK SIGNUP
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account? ",
                fontSize = 14.sp,
                color = Color(0xFF999999)
            )
            Text(
                text = "Sign Up",
                fontSize = 14.sp,
                color = Color(0xFF00FF88),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onCreateCharacterClick() }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
