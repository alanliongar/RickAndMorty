package com.devspace.rickandmorty.common.model

data class Character(
    val id: Int,
    val name: String,
    val image: String,
    val specie: String? = null,
    var isFavorite: Boolean = false,
)