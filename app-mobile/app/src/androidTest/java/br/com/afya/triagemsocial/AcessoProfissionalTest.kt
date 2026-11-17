package br.com.afya.triagemsocial

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import br.com.afya.triagemsocial.data.local.AgendamentoDados
import br.com.afya.triagemsocial.data.local.AgendamentoLocalDataSource
import br.com.afya.triagemsocial.data.local.AppDatabase
import br.com.afya.triagemsocial.data.repository.RoomAgendamentoRepository
import br.com.afya.triagemsocial.model.Especialidade
import br.com.afya.triagemsocial.model.StatusAgendamento
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AcessoProfissionalTest {
    @Test
    fun agendaProfissionalRestringeConsultaEAtualizacaoPorArea() = runBlocking {
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        val db = Room.inMemoryDatabaseBuilder(contexto, AppDatabase::class.java).build()
        try {
            val repo = RoomAgendamentoRepository(AgendamentoLocalDataSource(db))
            val psicologia = Especialidade.PSICOLOGIA
            val direito = Especialidade.DIREITO
            val proximoInicio = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, 1)
                if (get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) add(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 12)
                set(Calendar.MINUTE, 0)
            }.timeInMillis
            val dados = AgendamentoDados(
                nome = "Pessoa de teste",
                cpf = "01234567890",
                idade = "17",
                telefone = "",
                motivo = "Consulta",
                especialidade = psicologia,
                inicio = proximoInicio,
                nomeResponsavel = "Pessoa responsável",
                telefoneResponsavel = "46999999999"
            )
            val idPsicologia = repo.salvar(dados)
            val telefoneDireito = "4633333333"
            val idDireito = repo.salvar(dados.copy(
                especialidade = direito, idade = "18", telefone = telefoneDireito
            ))
            val agendaPsicologia = repo.observarDaArea(psicologia).first().single()
            val agendaDireito = repo.observarDaArea(direito).first().single()
            assertEquals(StatusAgendamento.PENDENTE, agendaPsicologia.status)
            assertEquals(idPsicologia, agendaPsicologia.id)
            assertTrue(agendaPsicologia.pacienteMenor)
            assertEquals(dados.nome, agendaPsicologia.nome)
            assertEquals("", agendaPsicologia.telefone)
            assertEquals(dados.nomeResponsavel, agendaPsicologia.nomeResponsavel)
            assertEquals(dados.telefoneResponsavel, agendaPsicologia.telefoneResponsavel)
            assertEquals(idDireito, agendaDireito.id)
            assertFalse(agendaDireito.pacienteMenor)
            assertEquals(telefoneDireito, agendaDireito.telefone)

            val tentativa = runCatching {
                repo.alterarStatus(psicologia, idDireito, StatusAgendamento.CANCELADO)
            }
            assertTrue(tentativa.exceptionOrNull() is IllegalArgumentException)
            assertEquals(StatusAgendamento.PENDENTE, repo.observarDaArea(direito).first().single().status)

            repo.alterarStatus(psicologia, idPsicologia, StatusAgendamento.CONCLUIDO)
            val atualizado = repo.observar().first().single { it.id == idPsicologia }
            assertEquals(StatusAgendamento.CONCLUIDO, atualizado.status)
            assertEquals(dados.inicio, repo.buscar(idPsicologia)?.inicio)
        } finally {
            db.close()
        }
    }
}
