package com.devspace.rickandmorty.list.data

import com.google.gson.annotations.SerializedName

data class CharacterListResponse(
    @SerializedName("results")
    val results: List<CharacterDto>
)
