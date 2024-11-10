package com.devspace.rickandmorty.list.data

import com.devspace.rickandmorty.common.model.Character

interface LocalDataSource {
    suspend fun getFavoriteCharacters(): List<Character>
    suspend fun updateFavoriteCharacter(character: Character)
    //suspend fun getUpdatedFavoriteCharacter(character: Character): Character
    //suspend fun getCharacterList(name: String? = null, specie: String? = null): List<Character>
    suspend fun updateCharacterList(characters: List<Character>)
}