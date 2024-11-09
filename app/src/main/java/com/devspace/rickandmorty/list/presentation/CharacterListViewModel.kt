package com.devspace.rickandmorty.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.devspace.rickandmorty.CharacterApplication
import com.devspace.rickandmorty.list.data.CharacterListRepository
import com.devspace.rickandmorty.list.presentation.ui.CharacterListUiState
import com.devspace.rickandmorty.list.presentation.ui.CharacterUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class CharacterListViewModel(private val repository: CharacterListRepository) :
    ViewModel() {
    private val _uiCharacterListUiState = MutableStateFlow(CharacterListUiState())
    val uiCharacterListUiState: StateFlow<CharacterListUiState> = _uiCharacterListUiState

    private fun fetchCharacterList() {
        _uiCharacterListUiState.value = CharacterListUiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getCharacterList()
            if (result.isSuccess) {
                val characters = result.getOrNull()?: emptyList()
                if (characters != null) {
                    val charactersUiDataList: List<CharacterUiData> =
                        characters.map { character ->
                            CharacterUiData(
                                id = character.id,
                                name = character.name,
                                image = character.image
                            )
                        }
                    _uiCharacterListUiState.value =
                        CharacterListUiState(charactersList = charactersUiDataList)
                } else {
                    _uiCharacterListUiState.value = CharacterListUiState(isError = true)
                }
            } else {
                val ex = result.exceptionOrNull()
                if (ex is UnknownHostException) {
                    _uiCharacterListUiState.value = CharacterListUiState(
                        isError = true,
                        errorMessage = "No internet connection"
                    )
                } else {
                    _uiCharacterListUiState.value = CharacterListUiState(isError = true)
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
                val application = checkNotNull(extras[APPLICATION_KEY])
                return CharacterListViewModel(
                    repository = (application as CharacterApplication).repository
                ) as T
            }
        }
    }
}