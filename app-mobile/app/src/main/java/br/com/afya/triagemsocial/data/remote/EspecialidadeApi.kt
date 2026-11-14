package br.com.afya.triagemsocial.data.remote

import retrofit2.http.GET

data class EspecialidadeDto(
    val id: String? = null,
    val nome: String? = null,
    val descricao: String? = null
)

interface EspecialidadeApi {
    @GET("especialidades")
    suspend fun listar(): List<EspecialidadeDto>
}