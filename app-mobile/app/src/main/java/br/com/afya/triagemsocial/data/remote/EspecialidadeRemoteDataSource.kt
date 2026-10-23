package br.com.afya.triagemsocial.data.remote

class EspecialidadeRemoteDataSource(
    private val api: EspecialidadeApi
) {
    suspend fun listar() = api.listar()
}
