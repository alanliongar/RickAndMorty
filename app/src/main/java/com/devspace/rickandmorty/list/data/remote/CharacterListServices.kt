package com.devspace.rickandmorty.list.data.remote

import com.devspace.rickandmorty.list.model.CharacterListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterListServices {
    @GET("character")
    suspend fun getAllCharacters(@Query("page") page: Int = 1): CharacterListResponse
}