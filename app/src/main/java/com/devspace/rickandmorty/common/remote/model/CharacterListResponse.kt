package com.devspace.rickandmorty.common.remote.model

import com.google.gson.annotations.SerializedName

data class CharacterListResponse(
    @SerializedName("results")
    val results: List<CharacterDto>
)
