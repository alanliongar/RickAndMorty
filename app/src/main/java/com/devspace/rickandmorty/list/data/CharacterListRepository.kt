package com.devspace.rickandmorty.list.data

import com.devspace.rickandmorty.common.model.Character
import com.devspace.rickandmorty.list.data.local.CharacterListLocalDataSource
import com.devspace.rickandmorty.list.data.remote.CharacterListRemoteDataSource

class CharacterListRepository(
    private val local: CharacterListLocalDataSource,
    private val remote: CharacterListRemoteDataSource,
) {
    suspend fun getCharacterList(): Result<List<Character>?> {
       return try {
            val result = remote.getCharacterList()
            if (result.isSuccess) {
                val characters = result.getOrNull()?: emptyList()
                if (characters.isNotEmpty()) {
                    local.updateCharacterList(characters)
                    return Result.success(local.getCharacterList())
                }else{
                    return result
                }
            } else {
                val localData = local.getCharacterList()
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