package br.com.afya.triagemsocial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.afya.triagemsocial.data.repository.EspecialidadeRepository
import br.com.afya.triagemsocial.model.Servico
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.io.IOException

sealed interface CatalogoState {
    data object Loading : CatalogoState
    data class Success(val itens: List<Servico>) : CatalogoState
    data class Error(val mensagem: String) : CatalogoState
}

class CatalogoViewModel(
    private val repository: EspecialidadeRepository
) : ViewModel() {
    private val _state = MutableStateFlow<CatalogoState>(
        CatalogoState.Loading
    )
    val state = _state.asStateFlow()
    private var consulta: Job? = null
    init { carregar() }

    fun carregar() {
        if (consulta?.isActive == true) return
        consulta = viewModelScope.launch {
            _state.value = CatalogoState.Loading
            _state.value = try {
                CatalogoState.Success(repository.listar())
            } catch (e: CancellationException) { throw e
            } catch (e: IOException) {
                CatalogoState.Error("Verifique sua conexão.")
            } catch (e: HttpException) {
                CatalogoState.Error("Serviço indisponível.")
            } catch (e: Exception) {
                CatalogoState.Error("Não foi possível ler os serviços.")
            }
        }
    }
}
