package com.devspace.rickandmorty.list.data

import com.devspace.rickandmorty.common.model.Character
import com.devspace.rickandmorty.list.data.local.CharacterListLocalDataSource
import com.devspace.rickandmorty.list.data.remote.CharacterListRemoteDataSource
import com.devspace.rickandmorty.list.presentation.ui.CharacterUiData

class CharacterListRepository(
    private val local: CharacterListLocalDataSource,
    private val remote: CharacterListRemoteDataSource,
) {
    suspend fun updateCharacterFavorite(character: CharacterUiData) {
        local.updateFavoriteCharacter(
            Character(
                id = character.id,
                name = character.name,
                image = character.image,
                specie = character.specie,
                isFavorite = !character.isFavorite
            )
        )
    }
    suspend fun getUpdatedFavoriteCharacter(character: CharacterUiData): CharacterUiData {
        return local.getUpdatedFavoriteCharacter(
            Character(
                id = character.id,
                name = character.name,
                image = character.image,
                specie = character.specie,
                isFavorite = character.isFavorite
            )
        )
    }

    suspend fun getFilteredCharacters(
        name: String? = null,
        specie: String? = null
    ): Result<List<Character>?> {
        return try {
            val result = remote.getFilteredCharacters(name = name, specie = specie)
            if (result.isSuccess) {
                val characters = result.getOrNull() ?: emptyList()
                if (characters.isNotEmpty()) {
                    val charactersSpecieced = characters.map {
                        Character(
                            id = it.id,
                            name = it.name,
                            image = it.image,
                            specie = specie,
                        )
                    }
                    local.updateCharacterList(charactersSpecieced)
                    return Result.success(local.getCharacterList(name = name, specie = specie))
                } else {
                    return result
                }
            } else {
                val localData = local.getCharacterList(name = name, specie = specie)
                if (localData.isEmpty()) {
                    return result
                } else {
                    return Result.success(localData)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }

}