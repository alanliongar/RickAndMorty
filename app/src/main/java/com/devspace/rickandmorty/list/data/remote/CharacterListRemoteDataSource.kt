package com.devspace.rickandmorty.list.data.remote

import android.accounts.NetworkErrorException
import com.devspace.rickandmorty.common.model.Character

class CharacterListRemoteDataSource(
    private val characterListService: CharacterListService
) {
    suspend fun getFilteredCharacters(
        name: String? = null,
        specie: String? = null
    ): Result<List<Character>?> {
        return try {
            val result = characterListService.getFilteredCharacters(name = name, species = specie)
            if (result.isSuccessful) {
                val characters = result.body()?.results?.map {
                    Character(
                        id = it.id,
                        name = it.name,
                        image = it.imageUrl,
                        specie = specie,
                    )
                }
                Result.success(characters)
            } else {
                Result.failure(NetworkErrorException(result.message()))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }
}