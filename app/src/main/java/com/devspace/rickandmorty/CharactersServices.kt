package com.devspace.rickandmorty

import retrofit2.http.GET

interface CharactersServices {

    @GET("character")
    suspend fun getAllCharacters(): List<CharacterEntity>
}