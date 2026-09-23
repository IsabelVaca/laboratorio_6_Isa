package mx.tec.avisos.data

import mx.tec.avisos.data.remote.AvisosApi
import mx.tec.avisos.data.remote.NuevoAvisoBody
import mx.tec.avisos.data.remote.toDomain
import mx.tec.avisos.domain.Aviso

/**
 * El tablón. No sabe nada de tokens: el interceptor firma las peticiones por
 * debajo, y si el servidor dice 401 o 403, la excepción sube tal cual.
 */
class AvisosRepository(private val api: AvisosApi) {

    suspend fun obtener(): List<Aviso> = api.getAvisos().map { it.toDomain() }

    suspend fun publicar(titulo: String, cuerpo: String): Aviso =
        api.crearAviso(NuevoAvisoBody(titulo.trim(), cuerpo.trim())).toDomain()
}
