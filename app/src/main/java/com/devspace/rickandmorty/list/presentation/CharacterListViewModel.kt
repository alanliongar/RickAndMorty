package com.devspace.rickandmorty.list.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.devspace.rickandmorty.common.RetrofitClient
import com.devspace.rickandmorty.list.data.remote.CharacterListService
import com.devspace.rickandmorty.list.model.CharacterListResponse
import com.devspace.rickandmorty.list.presentation.ui.CharacterListUiState
import com.devspace.rickandmorty.list.presentation.ui.CharacterUiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterListViewModel(private val characterListServices: CharacterListService) :
    ViewModel() {
    private val _uiCharacterList = MutableStateFlow(CharacterListUiState())
    val uiCharacterList: StateFlow<CharacterListUiState> = _uiCharacterList
    val characterApiService =
        RetrofitClient.retrofitInstance.create(CharacterListService::class.java)


    private fun fetchCharacterList() {
        _uiCharacterList.value = CharacterListUiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = characterApiService.getAllCharacters()
                if (response.isSuccessful) {
                    val characters = response.body()?.results ?: emptyList()
                    val charactersUiDataList: List<CharacterUiData> = characters.map { character ->
                        CharacterUiData(
                            id = character.id,
                            name = character.name,
                            image = character.imageUrl
                        )
                    }
                    _uiCharacterList.value = CharacterListUiState(characters = charactersUiDataList)
                } else {
                    Log.d(
                        "CharacterListViewModel",
                        "Request Error :: ${response.errorBody()}"
                    )
                    _uiCharacterList.value = CharacterListUiState(isError = true)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.d("CharacterListViewModel", ex.message.toString())
                _uiCharacterList.value = CharacterListUiState(isError = true)
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
                val characterListService =
                    RetrofitClient.retrofitInstance.create(CharacterListService::class.java)
                return CharacterListViewModel(characterListService) as T
            }
        }
    }
}