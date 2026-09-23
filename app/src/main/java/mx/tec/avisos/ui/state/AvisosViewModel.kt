package mx.tec.avisos.ui.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.tec.avisos.data.AvisosRepository
import mx.tec.avisos.domain.Aviso
import retrofit2.HttpException
import java.io.IOException

/** La lista del tablón. La misma forma que la lista de la Práctica 4. */
class AvisosViewModel(private val repository: AvisosRepository) : ViewModel() {

    var avisos by mutableStateOf<UiState<List<Aviso>>>(UiState.Cargando)
        private set

    fun cargar() {
        viewModelScope.launch {
            avisos = UiState.Cargando
            avisos = try {
                UiState.Exito(repository.obtener())
            } catch (e: IOException) {
                UiState.Error("No hay conexión. Revisa tu internet.")
            } catch (e: HttpException) {
                UiState.Error(mensajeDe(e))
            }
        }
    }
}
