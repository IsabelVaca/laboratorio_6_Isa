# Bitácora — Práctica 6: Avisos

Isabel Vaca

## Ejercicio 0 — ¿Qué se guarda, y dónde?

| Dato | ¿Se guarda? | ¿Con qué protección? |
|---|---|---|
| Contraseña | No | Se manda una vez y se olvida. El servidor la guarda con función lenta y sal. |
| Token de acceso | Sí | Cifrado con la llave del Keystore. |
| Token de refresco | Sí | Cifrado con la llave del Keystore. |
| Usuario y rol | Sí | En claro. No son secretos; el servidor no les cree nada. |
| Código de profesor | No | Se usa una vez, al registrarse. |
| Fecha de expiración | Sí | En claro. Solo alimenta el contador del banner. |



## Ejercicio B3 — ¿Por dónde se sale un token?

- **Logcat** — con `HEADERS` o `BODY`, el header sale entero. Lo evita `redactHeader("Authorization")`.
- **La URL** — como query param quedaría en logs y en el `Referer`. Lo evita mandarlo siempre en el header.
- **Portapapeles** — cualquier app puede leerlo. Lo evita no exponer el token en la UI.
- **Respaldos y reportes de error** — se restauran en otro teléfono con la sesión adentro. Lo evita `android:allowBackup="false"`.

## Ejercicio C2 — Reconstruir el 403

1. `PublicarScreen` llamó a `onPublicar` → `viewModel.publicar { … }`.
2. `PublicarViewModel` puso `enviando = true` y llamó a `repository.publicar(...)`.
3. `AvisosRepository` llamó a `api.crearAviso(...)`.
4. `AuthInterceptor` pidió `tokenActual()`, el repositorio descifró el token del store y lo pegó en `Authorization`.
5. El servidor verificó la firma, leyó `rol: "alumno"` **del token**, y respondió 403.
6. Retrofit lo volvió `HttpException`, el ViewModel lo atrapó y `uiState.error` lo llevó a la pantalla. `alTerminar()` nunca se llamó, así que el formulario se quedó abierto.

**¿Y si hubiera cambiado el rol en DataStore?** No cambia nada. Eso altera lo que la app cree, no lo que el token dice, y el servidor nunca vio mi archivo. Para engañarlo tendría que alterar el token, y eso da 401 `token_invalido`.

El cliente esconde el botón por cortesía. El servidor decide. Cuando no coinciden, gana el servidor.
