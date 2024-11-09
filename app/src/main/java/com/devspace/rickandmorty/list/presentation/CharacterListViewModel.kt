package com.devspace.rickandmorty.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.devspace.rickandmorty.common.RetrofitClient
import com.devspace.rickandmorty.list.data.CharacterListRepository
import com.devspace.rickandmorty.list.data.remote.CharacterListService
import com.devspace.rickandmorty.list.presentation.ui.CharacterListUiState
import com.devspace.rickandmorty.list.presentation.ui.CharacterUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class CharacterListViewModel(private val repository: CharacterListRepository) :
    ViewModel() {
    private val _uiCharacterList = MutableStateFlow(CharacterListUiState())
    val uiCharacterList: StateFlow<CharacterListUiState> = _uiCharacterList

    private fun fetchCharacterList() {
        _uiCharacterList.value = CharacterListUiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getCharacterList()
            if (result.isSuccess) {
                val characters = result.getOrNull()?.results ?: emptyList()
                if (characters != null) {
                    val charactersUiDataList: List<CharacterUiData> =
                        characters.map { character ->
                            CharacterUiData(
                                id = character.id,
                                name = character.name,
                                image = character.imageUrl
                            )
                        }
                    _uiCharacterList.value = CharacterListUiState(charactersList = charactersUiDataList)
                } else {
                    _uiCharacterList.value = CharacterListUiState(isError = true)
                }
            } else {
                val ex = result.exceptionOrNull()
                if (ex is UnknownHostException) {
                    _uiCharacterList.value = CharacterListUiState(isError = true, errorMessage = "No internet connection")
                } else {
                    _uiCharacterList.value = CharacterListUiState(isError = true)
                }
            }
        }
    }

    init {
        fetchCharacterList()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val characterListService = RetrofitClient.retrofitInstance.create(CharacterListService::class.java)
                val characterListRepository = CharacterListRepository(characterListService)
                return CharacterListViewModel(characterListRepository) as T
            }
        }
    }
}