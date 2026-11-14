package br.com.afya.triagemsocial.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AgendamentoDao {
    @Query("SELECT * FROM agendamentos ORDER BY inicio")
    fun observar(): Flow<List<AgendamentoEntity>>

    @Query("SELECT * FROM agendamentos WHERE especialidade = :area ORDER BY inicio")
    fun observarDaArea(area: String): Flow<List<AgendamentoEntity>>

    @Query("SELECT * FROM agendamentos WHERE id = :id AND especialidade = :area")
    suspend fun buscarDaArea(id: Int, area: String): AgendamentoEntity?

    @Query("UPDATE agendamentos SET status = :status WHERE id = :id AND especialidade = :area")
    suspend fun atualizarStatusDaArea(id: Int, area: String, status: String): Int

    @Query("SELECT * FROM agendamentos WHERE id = :id")
    suspend fun buscar(id: Int): AgendamentoEntity?

    @Insert
    suspend fun inserir(item: AgendamentoEntity): Long

    @Update
    suspend fun atualizar(item: AgendamentoEntity): Int

    @Query("DELETE FROM agendamentos WHERE id = :id")
    suspend fun excluir(id: Int): Int

    @Query("""
        SELECT EXISTS(
          SELECT 1 FROM agendamentos
          WHERE especialidade = :area AND id != :ignorarId
            AND status != 'CANCELADO'
            AND inicio > :limiteInferior
            AND inicio < :limiteSuperior
        )
    """)
    suspend fun existeConflito(
        area: String,
        ignorarId: Int,
        limiteInferior: Long,
        limiteSuperior: Long
    ): Boolean
}
