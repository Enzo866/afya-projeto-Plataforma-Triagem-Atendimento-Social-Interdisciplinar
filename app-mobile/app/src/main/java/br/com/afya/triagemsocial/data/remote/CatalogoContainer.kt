package br.com.afya.triagemsocial.data.remote

import br.com.afya.triagemsocial.data.repository.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object CatalogoContainer {
    private const val BASE_URL =
        "http://127.0.0.1:8080/"

    private val cliente = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(cliente)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(EspecialidadeApi::class.java)

    val repository: EspecialidadeRepository =
        ApiEspecialidadeRepository(
            EspecialidadeRemoteDataSource(api)
        )
}
