package com.devspace.rickandmorty.common.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterListEntity(
    @PrimaryKey/*(autoGenerate = true) - Sem gerar, pq vem da API, nao é pra gerar nada*/
    val id: Int,
    val name: String,
    val image: String,
    val specie: String? = null,
    var isFavorite: Boolean = false
)