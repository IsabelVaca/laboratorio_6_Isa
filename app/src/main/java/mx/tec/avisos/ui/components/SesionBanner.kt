package mx.tec.avisos.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import mx.tec.avisos.domain.Rol
import mx.tec.avisos.domain.Sesion
import mx.tec.avisos.ui.theme.AvisosTheme

/**
 * Quién eres y cuánto le queda a tu token de acceso. Existe para que la
 * expiración se VEA: sin esto, un 401 a los cinco minutos parece un bug.
 */
@Composable
fun SesionBanner(sesion: Sesion, modifier: Modifier = Modifier) {
    var restantes by remember(sesion.expiraEn) { mutableLongStateOf(sesion.segundosRestantes()) }

    // Un tic por segundo mientras el banner esté en pantalla. Se reinicia solo
    // cuando llega un token nuevo, porque `expiraEn` cambia.
    LaunchedEffect(sesion.expiraEn) {
        while (true) {
            restantes = sesion.segundosRestantes()
            delay(1_000)
        }
    }

    val vencido = restantes <= 0
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = if (vencido) MaterialTheme.colorScheme.errorContainer
        else MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (vencido) Icons.Default.Lock else Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.padding(end = 12.dp)
            )
            Column {
                Text(
                    text = "${sesion.usuario} · ${sesion.rol.name.lowercase()}",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = if (vencido) "El acceso venció. La siguiente petición lo renueva… o te saca."
                    else "El acceso vence en ${restantes.comoMinutos()}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

private fun Long.comoMinutos(): String = "%d:%02d".format(this / 60, this % 60)

@Preview(showBackground = true)
@Composable
private fun SesionBannerPreview() {
    AvisosTheme {
        SesionBanner(
            Sesion("a01234567", Rol.ALUMNO, "x", "y", System.currentTimeMillis() / 1000 + 200)
        )
    }
}
