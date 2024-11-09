package com.devspace.rickandmorty.list.data

import android.accounts.NetworkErrorException
import com.devspace.rickandmorty.list.data.local.CharacterListLocalDataSource
import com.devspace.rickandmorty.list.data.remote.CharacterListRemoteDataSource
import com.devspace.rickandmorty.list.data.remote.CharacterListService
import com.devspace.rickandmorty.list.model.CharacterListResponse

class CharacterListRepository(
    private val local: CharacterListLocalDataSource,
    private val remote: CharacterListRemoteDataSource,
) {
    suspend fun getCharacterList(): Result<CharacterListResponse?> {
       return try {
            val result = characterListService.getAllCharacters()
            if (result.isSuccessful) {
                Result.success(result.body())
            } else {
                Result.failure(NetworkErrorException(result.message()))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.failure(ex)
        }
    }
}