package com.devspace.rickandmorty.list.data.local

import com.devspace.rickandmorty.common.local.CharacterDao
import com.devspace.rickandmorty.common.model.Character

class CharacterListLocalDataSource(
    private val dao: CharacterDao
) {

    suspend fun getCharacterList(): List<Character> {
        return dao.getAll().map {
            Character(
                id = it.id,
                name = it.name,
                image = it.image
            )
        }
    }
}