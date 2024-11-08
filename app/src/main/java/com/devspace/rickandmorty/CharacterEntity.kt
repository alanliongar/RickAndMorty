package com.devspace.rickandmorty

import androidx.room.PrimaryKey

data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
)
