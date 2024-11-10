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

    suspend fun getAllCharacters(): List<Character> {
        return local.getCharacterList()
    }

    suspend fun getFilteredCharacters(
        name: String? = null,
        specie: String? = null
    ): Result<List<Character>?> {
        return try {
            //Abordagem levemente diferente: ele verifica se existe algum favorito antes de sair jogando dados da API pro banco;
            //Depois de guardar os favoritos, ele chama a API, alimenta o banco e devolve o status de favorito pros ids que já o tinham.
            //Segura essa abordagem por enquanto.
            val existingFavorites = local.getFavoriteCharacters()?: emptyList()
            val result = remote.getFilteredCharacters(name = name, specie = specie)
            if (result.isSuccess) {
                val characters = result.getOrNull() ?: emptyList()

                if (characters.isNotEmpty()) {
                    val charactersWithFavorites = characters.map {
                        val isFavorite = existingFavorites?.any { favorite -> favorite.id == it.id }
                        it.copy(isFavorite = if (isFavorite?: false) true else false)
                    }
                    local.updateCharacterList(charactersWithFavorites)
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