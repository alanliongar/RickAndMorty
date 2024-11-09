package com.devspace.rickandmorty.list.model

import androidx.room.PrimaryKey

data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
)