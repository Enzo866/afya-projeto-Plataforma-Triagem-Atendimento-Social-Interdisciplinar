package br.com.afya.triagemsocial.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*
import java.io.IOException

private val Context.preferencias by preferencesDataStore(
    name = "preferencias_afya"
)

class PreferenciasDataSource(context: Context) {
    private val store = context.applicationContext.preferencias
    private val chave = stringPreferencesKey("filtro_status")

    val filtro: Flow<String> = store.data
        .catch { erro ->
            if (erro is IOException) emit(emptyPreferences())
            else throw erro
        }
        .map { it[chave] ?: "TODOS" }

    suspend fun salvarFiltro(valor: String) {
        store.edit { it[chave] = valor }
    }
}
