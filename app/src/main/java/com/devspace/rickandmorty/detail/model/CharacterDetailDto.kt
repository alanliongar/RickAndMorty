package com.devspace.rickandmorty.detail.model

data class CharacterDetailDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String,
    val origin: Origin,
    val location: Location,
){
    //não precisa buscar as url
    data class Origin(val name: String)
    data class Location(val name: String)
}