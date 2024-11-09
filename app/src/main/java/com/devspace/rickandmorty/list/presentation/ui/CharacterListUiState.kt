package com.devspace.rickandmorty.list.presentation.ui

data class CharacterListUiState(
    var charactersList: List<CharacterUiData> = emptyList(),
    var isLoading: Boolean = false,
    var isError: Boolean = false,
    var errorMessage: String? = null
)

data class CharacterUiData(
    val id: Int,
    val name: String,
    val image: String,
    val specie: String? = null,
    var isFavorite: Boolean = false
)