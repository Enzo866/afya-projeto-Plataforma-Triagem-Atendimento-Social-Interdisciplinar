package br.com.afya.triagemsocial.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AgendamentoDao {
    @Query("SELECT * FROM agendamentos ORDER BY inicio")
    fun observar(): Flow<List<AgendamentoEntity>>

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
