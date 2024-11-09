package com.devspace.rickandmorty.list.data.local

import com.devspace.rickandmorty.common.local.CharacterDao
import com.devspace.rickandmorty.common.local.CharacterListEntity
import com.devspace.rickandmorty.common.model.Character
import com.devspace.rickandmorty.list.presentation.ui.CharacterUiData

class CharacterListLocalDataSource(
    private val dao: CharacterDao
) {
    suspend fun getFavoriteCharacters(): List<Character> {
        val favoriteCharacters = dao.getFavoriteCharacters()
        return favoriteCharacters.map {
            Character(
                id = it.id,
                name = it.name,
                image = it.image,
                specie = it.specie,
                isFavorite = it.isFavorite,
            )
        }
    }

    suspend fun updateFavoriteCharacter(character: Character) {
        dao.updateCharacter(
            CharacterListEntity(
                id = character.id,
                name = character.name,
                image = character.image,
                specie = character.specie,
                isFavorite = character.isFavorite
            )
        )
    }

    suspend fun getUpdatedFavoriteCharacter(character: Character): CharacterUiData {
        val characterListEntity = dao.getCharacterById(id = character.id)
        return CharacterUiData(
            id = characterListEntity.id,
            name = characterListEntity.name,
            image = characterListEntity.image,
            specie = characterListEntity.specie,
            isFavorite = characterListEntity.isFavorite
        )
        //retorna um characterlistentity
    }

    suspend fun getCharacterList(
        name: String? = null,
        specie: String? = null
    ): List<Character> {
        return dao.getFilteredCharacters(name = name, specie = specie)
            .map { //query no banco de dados!
                Character(
                    id = it.id,
                    name = it.name,
                    image = it.image,
                    specie = specie,
                    isFavorite = it.isFavorite
                )
            }
    }

    suspend fun updateCharacterList(characters: List<Character>) {
        dao.insertAll(characters.map {
            CharacterListEntity(
                id = it.id,
                name = it.name,
                image = it.image,
                specie = it.specie,
                isFavorite = it.isFavorite
            )
        })
    }
}