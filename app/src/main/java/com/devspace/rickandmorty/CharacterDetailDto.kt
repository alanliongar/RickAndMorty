package com.devspace.rickandmorty

import androidx.compose.ui.graphics.Color

data class CharacterDetailDto(
    val id: Int,
    val name: String,
    val status: String,
    val type: String,
    val gender: String,
    val image: String,
    val origin: Origin,
    val location: Location,
){
    data class Origin(val name: String, val url: String)
    data class Location(val name: String, val url: String)
}