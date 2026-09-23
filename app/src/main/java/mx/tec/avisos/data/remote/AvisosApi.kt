package mx.tec.avisos.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AvisosApi {

    // ---- sin token: son las llamadas que te dan uno
    @POST("auth/register")
    suspend fun register(@Body body: Credenciales): TokensDto

    @POST("auth/login")
    suspend fun login(@Body body: Credenciales): TokensDto

    @POST("auth/refresh")
    suspend fun refresh(@Body body: RefreshBody): TokensDto

    @POST("auth/logout")
    suspend fun logout(@Body body: RefreshBody)

    // ---- con token: el `Authorization: Bearer …` lo pone el interceptor
    @GET("auth/me")
    suspend fun me(): MeDto

    @GET("avisos")
    suspend fun getAvisos(): List<AvisoDto>

    @POST("avisos")
    suspend fun crearAviso(@Body body: NuevoAvisoBody): AvisoDto

    @DELETE("avisos/{id}")
    suspend fun borrarAviso(@Path("id") id: Int): Response<Unit>
}
