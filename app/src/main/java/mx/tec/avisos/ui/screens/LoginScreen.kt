package mx.tec.avisos.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.tec.avisos.domain.CredencialesValidator
import mx.tec.avisos.ui.state.LoginUiState
import mx.tec.avisos.ui.theme.AvisosTheme

/**
 * La pantalla es tonta: recibe el estado y avisa qué tecleó el usuario. No
 * sabe de tokens, ni de red, ni de dónde se guarda nada.
 */
@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onUsuarioChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onCodigoProfesorChange: (String) -> Unit,
    onAlternarModo: () -> Unit,
    onEnviar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Avisos", style = MaterialTheme.typography.headlineLarge)
            Text(
                text = if (uiState.modoRegistro) "Crea tu cuenta" else "Entra con tu cuenta",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.usuario,
                onValueChange = onUsuarioChange,
                label = { Text("Usuario") },
                supportingText = { Text("Tu matrícula, en minúsculas") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                supportingText = { Text("Al menos ${CredencialesValidator.PASSWORD_MIN} caracteres") },
                singleLine = true,
                // Los puntos no son adorno: es lo que impide que alguien la lea por encima del hombro.
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState.modoRegistro) {
                OutlinedTextField(
                    value = uiState.codigoProfesor,
                    onValueChange = onCodigoProfesorChange,
                    label = { Text("Código de profesor (opcional)") },
                    supportingText = { Text("Sin código, la cuenta es de alumno") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            val error = uiState.error
            if (error != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onEnviar,
                enabled = uiState.puedeEnviar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    when {
                        uiState.enviando -> "Un momento…"
                        uiState.modoRegistro -> "Crear cuenta"
                        else -> "Entrar"
                    }
                )
            }

            TextButton(onClick = onAlternarModo) {
                Text(if (uiState.modoRegistro) "Ya tengo cuenta" else "¿Primera vez? Crea tu cuenta")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    AvisosTheme {
        LoginScreen(
            uiState = LoginUiState(usuario = "a01234567", password = "secreta123"),
            onUsuarioChange = {}, onPasswordChange = {}, onCodigoProfesorChange = {},
            onAlternarModo = {}, onEnviar = {}
        )
    }
}

@Preview(showBackground = true, name = "Registro con error")
@Composable
private fun RegistroPreview() {
    AvisosTheme {
        LoginScreen(
            uiState = LoginUiState(usuario = "a01234567", modoRegistro = true, error = "El usuario \"a01234567\" ya existe"),
            onUsuarioChange = {}, onPasswordChange = {}, onCodigoProfesorChange = {},
            onAlternarModo = {}, onEnviar = {}
        )
    }
}
