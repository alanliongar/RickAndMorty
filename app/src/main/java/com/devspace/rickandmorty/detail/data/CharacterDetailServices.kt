package com.devspace.rickandmorty.detail.data

import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterDetailServices {
    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharacterDetailDto
}