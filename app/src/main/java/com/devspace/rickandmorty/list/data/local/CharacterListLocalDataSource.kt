package com.devspace.rickandmorty.list.data.local

import com.devspace.rickandmorty.common.local.CharacterDao
import com.devspace.rickandmorty.common.local.CharacterListEntity
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

    suspend fun updateCharacterList(characters: List<Character>) {
        dao.insertAll(characters.map {
            CharacterListEntity(
                id = it.id,
                name = it.name,
                image = it.image
            )
        })
    }
}