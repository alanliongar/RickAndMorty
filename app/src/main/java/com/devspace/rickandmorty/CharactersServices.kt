package com.devspace.rickandmorty

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersServices {
    @GET("character")
    suspend fun getAllCharacters(@Query("page") page: Int = 1): CharacterListResponse

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharacterDetailDto
}