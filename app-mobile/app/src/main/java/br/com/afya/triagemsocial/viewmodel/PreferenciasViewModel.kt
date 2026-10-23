package br.com.afya.triagemsocial.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.afya.triagemsocial.data.repository.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PreferenciasViewModel(
    private val repository: PreferenciasRepository
) : ViewModel() {
    val filtro = repository.filtro.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000),
        FiltroStatus.TODOS
    )
    private val _erro = MutableStateFlow<String?>(null)
    val erro = _erro.asStateFlow()

    fun selecionar(filtro: FiltroStatus) {
        viewModelScope.launch {
            try {
                repository.salvar(filtro)
                _erro.value = null
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _erro.value = "Não foi possível salvar o filtro."
            }
        }
    }
}
