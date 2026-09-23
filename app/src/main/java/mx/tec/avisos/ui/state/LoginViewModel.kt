package mx.tec.avisos.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import mx.tec.avisos.domain.CredencialesValidator

/**
 * Lo que el usuario lleva tecleado en el login. La contraseña vive aquí solo
 * mientras se escribe: al entrar se manda y se olvida. Nunca se guarda.
 */
data class LoginUiState(
    val usuario: String = "",
    val password: String = "",
    val codigoProfesor: String = "",
    val modoRegistro: Boolean = false,
    val enviando: Boolean = false,
    val error: String? = null
) {
    val puedeEnviar: Boolean =
        CredencialesValidator.sonValidas(usuario, password) && !enviando
}

class LoginViewModel : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onUsuarioChange(texto: String) {
        uiState = uiState.copy(usuario = texto, error = null)
    }

    fun onPasswordChange(texto: String) {
        uiState = uiState.copy(password = texto, error = null)
    }

    fun onCodigoProfesorChange(texto: String) {
        uiState = uiState.copy(codigoProfesor = texto, error = null)
    }

    fun alternarModo() {
        uiState = uiState.copy(modoRegistro = !uiState.modoRegistro, error = null)
    }

    fun enviar() {
        // Todavía no hay a quién mandarle las credenciales. Eso es el Bloque A.
    }
}
