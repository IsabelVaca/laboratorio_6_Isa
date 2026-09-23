package mx.tec.avisos.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.tec.avisos.data.AvisosRepository
import mx.tec.avisos.domain.AvisoValidator
import retrofit2.HttpException
import java.io.IOException

data class PublicarUiState(
    val titulo: String = "",
    val cuerpo: String = "",
    val enviando: Boolean = false,
    val error: String? = null
) {
    val puedePublicar: Boolean = AvisoValidator.esValido(titulo, cuerpo) && !enviando

    val caracteresRestantes: Int = AvisoValidator.CUERPO_MAX - cuerpo.trim().length
}

class PublicarViewModel(private val repository: AvisosRepository) : ViewModel() {

    var uiState by mutableStateOf(PublicarUiState())
        private set

    fun onTituloChange(texto: String) {
        if (texto.length <= AvisoValidator.TITULO_MAX) uiState = uiState.copy(titulo = texto, error = null)
    }

    fun onCuerpoChange(texto: String) {
        if (texto.length <= AvisoValidator.CUERPO_MAX) uiState = uiState.copy(cuerpo = texto, error = null)
    }

    /** `alTerminar` se llama solo si el servidor aceptó el aviso. Un 403 se queda a la vista. */
    fun publicar(alTerminar: () -> Unit) {
        if (!uiState.puedePublicar) return
        viewModelScope.launch {
            uiState = uiState.copy(enviando = true, error = null)
            try {
                repository.publicar(uiState.titulo, uiState.cuerpo)
                uiState = uiState.copy(enviando = false)
                alTerminar()
            } catch (e: IOException) {
                uiState = uiState.copy(enviando = false, error = "No hay conexión. El aviso no se publicó.")
            } catch (e: HttpException) {
                uiState = uiState.copy(enviando = false, error = mensajeDe(e))
            }
        }
    }
}
