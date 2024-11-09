package com.devspace.rickandmorty.list.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.devspace.rickandmorty.common.RetrofitClient
import com.devspace.rickandmorty.list.data.remote.CharacterListService
import com.devspace.rickandmorty.list.model.CharacterListResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterListViewModel(private val characterListServices: CharacterListService) :
    ViewModel() {
    private val _uiCharacterList = MutableStateFlow<CharacterListResponse?>(null)
    val uiCharacterList: StateFlow<CharacterListResponse?> = _uiCharacterList
    val characterApiService =
        RetrofitClient.retrofitInstance.create(CharacterListService::class.java)


    private fun fetchCharacterList() {
        viewModelScope.launch {
            try {
                val response = characterApiService.getAllCharacters()
                if (response.results.isNotEmpty()) {
                    _uiCharacterList.value = response
                    Log.d("CharacterListViewModel", "Success")
                } else {
                    _uiCharacterList.value = null
                    Log.d("CharacterListViewModel", "EmptyCall")
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.d("CharacterListViewModel", ex.message.toString())
                _uiCharacterList.value = null
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