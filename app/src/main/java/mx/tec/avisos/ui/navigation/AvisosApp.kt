package mx.tec.avisos.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.tec.avisos.ui.screens.LoginScreen
import mx.tec.avisos.ui.state.AppViewModelProvider
import mx.tec.avisos.ui.state.LoginViewModel

/**
 * La raíz de la app. Hoy solo existe el login: no hay sesión que consultar.
 * Durante la práctica esto se convierte en un `when` sobre la sesión, y el
 * tablón (`AvisosNavHost`) aparece solo cuando hay una.
 */
@Composable
fun AvisosApp() {
    val loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)

    LoginScreen(
        uiState = loginViewModel.uiState,
        onUsuarioChange = loginViewModel::onUsuarioChange,
        onPasswordChange = loginViewModel::onPasswordChange,
        onCodigoProfesorChange = loginViewModel::onCodigoProfesorChange,
        onAlternarModo = loginViewModel::alternarModo,
        onEnviar = loginViewModel::enviar
    )
}
