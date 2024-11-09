package com.devspace.rickandmorty.list.data

import androidx.room.PrimaryKey

data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
)