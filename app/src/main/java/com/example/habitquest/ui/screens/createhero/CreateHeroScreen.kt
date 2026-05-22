package com.example.habitquest.ui.screens.createhero

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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habitquest.ui.theme.HabitQuestTheme
import com.example.habitquest.ui.screens.createhero.EstadoRegistro
import com.example.habitquest.ui.screens.createhero.RegistroViewModel

@Composable
fun CreateHeroScreen(
    viewModel: RegistroViewModel,
    onBackClick: () -> Unit = {},
    onRegistrationSuccess: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    val showPassword = remember { mutableStateOf(false) }

    // Observar estados del ViewModel
    val estadoRegistro = viewModel.estadoRegistro.collectAsState()
    val nombre = viewModel.nombre.collectAsState()
    val correo = viewModel.correo.collectAsState()
    val contraseña = viewModel.contraseña.collectAsState()
    val clase = viewModel.clase.collectAsState()

    // Efecto para manejar navegación después de registro exitoso
    LaunchedEffect(estadoRegistro.value) {
        if (estadoRegistro.value is EstadoRegistro.Exitoso) {
            onRegistrationSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1a3a2a)) // Fondo verde oscuro
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // HEADER CON BOTÓN ATRÁS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp),
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
                text = "Create Hero",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // AVATAR CÍRCULO CON EFECTO DE SOMBRA
        Box(
            modifier = Modifier.size(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .border(
                        width = 6.dp,
                        color = Color(0xFFB8956A),
                        shape = CircleShape
                    )
                    .background(
                        color = Color(0xFF00FF88),
                        shape = CircleShape
                    )
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color.White, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👤",
                        fontSize = 60.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // TEXTO "TAP TO UPLOAD"
        Text(
            text = "Tap to upload",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(2.dp))

        // SUBTÍTULO VERDE
        Text(
            text = "Set your hero appearance",
            fontSize = 14.sp,
            color = Color(0xFF00FF88),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        // HERO NAME LABEL
        Text(
            text = "Hero Name",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // HERO NAME INPUT
        OutlinedTextField(
            value = nombre.value,
            onValueChange = { viewModel.actualizarNombre(it) },
            placeholder = {
                Text(
                    text = "Ex: Arthur Pendragon",
                    color = Color(0xFF666666),
                    fontSize = 14.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00FF88),
                unfocusedBorderColor = Color(0xFF00AA66),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color(0xFFCCCCCC)
            ),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                    text = "email@example.com",
                    color = Color(0xFF666666),
                    fontSize = 14.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00FF88),
                unfocusedBorderColor = Color(0xFF00AA66),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color(0xFFCCCCCC)
            ),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // SECRET PASSWORD LABEL
        Text(
            text = "Secret Password",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // SECRET PASSWORD INPUT
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
                unfocusedBorderColor = Color(0xFF00AA66),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color(0xFFCCCCCC)
            ),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // CHOOSE YOUR CLASS LABEL
        Text(
            text = "CHOOSE YOUR CLASS",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.Start),
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // CLASES EN FILA
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ClassCard(
                name = "Guerrero",
                icon = "⚔️",
                isSelected = clase.value == "GUERRERO",
                onClick = { viewModel.actualizarClase("GUERRERO") },
                modifier = Modifier.weight(1f)
            )

            ClassCard(
                name = "Mago",
                icon = "🔮",
                isSelected = clase.value == "MAGO",
                onClick = { viewModel.actualizarClase("MAGO") },
                modifier = Modifier.weight(1f)
            )

            ClassCard(
                name = "Pícaro",
                icon = "🗡️",
                isSelected = clase.value == "PICARO",
                onClick = { viewModel.actualizarClase("PICARO") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // BOTÓN CREATE ACCOUNT
        Button(
            onClick = { viewModel.registrarUsuario() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00FF88)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Create Account",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1a3a2a)
            )
        }

        // Mostrar mensaje de error si existe
        if (estadoRegistro.value is EstadoRegistro.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = (estadoRegistro.value as EstadoRegistro.Error).mensaje,
                color = Color.Red,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // LINK LOGIN
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account? ",
                fontSize = 14.sp,
                color = Color(0xFF999999)
            )
            Text(
                text = "Login",
                fontSize = 14.sp,
                color = Color(0xFF00FF88),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onLoginClick() }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ClassCard(
    name: String,
    icon: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(
                width = if (isSelected) 3.dp else 2.dp,
                color = if (isSelected) Color(0xFF00FF88) else Color(0xFF00AA66),
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = Color(0xFF0d6b4f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ICONO CLASE
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = Color(0xFF053d2e),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = icon,
                fontSize = 36.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // NOMBRE CLASE
        Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        // LÍNEA VERDE DEBAJO SI ESTÁ SELECCIONADA
        if (isSelected) {
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(Color(0xFF00FF88), shape = RoundedCornerShape(2.dp))
            )
        }
    }
}
