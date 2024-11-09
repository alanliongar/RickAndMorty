package com.devspace.rickandmorty.detail.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.devspace.rickandmorty.common.RetrofitClient
import com.devspace.rickandmorty.detail.data.CharacterDetailService
import com.devspace.rickandmorty.detail.model.CharacterDetailDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterDetailViewModel(private val characterDetailService: CharacterDetailService) :
    ViewModel() {

        private val _uiCharacterDetail = MutableStateFlow<CharacterDetailDto?>(null)
        val uiCharacterDetail: StateFlow<CharacterDetailDto?> = _uiCharacterDetail

        fun fetchCharacterDetail(characterId: Int) {
            viewModelScope.launch {
                try {
                    val response = characterDetailService.getCharacterById(characterId.toInt())
                    _uiCharacterDetail.value = response
                    Log.d("CharacterDetailScreen", "Success")
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    Log.d("CharacterDetailScreen", ex.message.toString())
                    _uiCharacterDetail.value = null
                }
            }
        }





        companion object{
            val Factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory{
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                    val characterDetailService = RetrofitClient.retrofitInstance.create(CharacterDetailService::class.java)
                    return CharacterDetailViewModel(characterDetailService) as T
                }
            }
        }
}