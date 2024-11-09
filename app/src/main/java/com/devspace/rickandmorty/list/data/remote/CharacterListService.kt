package com.devspace.rickandmorty.list.data.remote

import com.devspace.rickandmorty.common.remote.model.CharacterListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterListService {
    @GET("character")
    suspend fun getAllCharacters(@Query("page") page: Int = 1): Response<CharacterListResponse>

    @GET("character")
    suspend fun getFilteredCharacters(
        @Query("name") name: String? = null,
        @Query("species") species: String? = null,
        @Query("page") page: Int = 1,
    ): Response<CharacterListResponse>
}