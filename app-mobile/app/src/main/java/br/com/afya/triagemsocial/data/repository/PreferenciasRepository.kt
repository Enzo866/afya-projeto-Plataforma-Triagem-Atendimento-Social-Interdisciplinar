package br.com.afya.triagemsocial.data.repository

import br.com.afya.triagemsocial.data.local.PreferenciasDataSource
import br.com.afya.triagemsocial.model.StatusAgendamento
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

enum class FiltroStatus { TODOS, PENDENTE, AGENDADO,
    CANCELADO, CONCLUIDO }

fun FiltroStatus.aceita(status: StatusAgendamento): Boolean =
    this == FiltroStatus.TODOS || name == status.name

class PreferenciasRepository(
    private val local: PreferenciasDataSource,
    private val io: CoroutineDispatcher = Dispatchers.IO
) {
    val filtro = local.filtro.map { salvo ->
        FiltroStatus.entries.firstOrNull { it.name == salvo }
            ?: FiltroStatus.TODOS
    }

    suspend fun salvar(filtro: FiltroStatus) = withContext(io) {
        local.salvarFiltro(filtro.name)
    }
}
