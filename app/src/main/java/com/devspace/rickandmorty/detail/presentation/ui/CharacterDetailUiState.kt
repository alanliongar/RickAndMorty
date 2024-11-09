package com.devspace.rickandmorty.detail.presentation.ui

import com.devspace.rickandmorty.detail.model.CharacterDetailDto

data class CharacterDetailUiState(
    var character: CharacterDetailDto? = null,
    var isLoading: Boolean = false,
    var isError: Boolean = false,
    var errorMessage: String? = null
)
