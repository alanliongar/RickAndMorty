package com.devspace.rickandmorty.list.presentation.ui

data class CharacterListUiState(
    var characters: List<CharacterUiData> = emptyList(),
    var isLoading: Boolean = false,
    var isError: Boolean = false,
    var errorMessage: String? = null
) {
}

data class CharacterUiData(
    val id: Int,
    val name: String,
    val image: String
)