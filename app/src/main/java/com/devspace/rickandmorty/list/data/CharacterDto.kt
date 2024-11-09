package com.devspace.rickandmorty.list.data

data class CharacterDto(
    val id: Int,
    val name: String,
){
    val imageUrl: String
        get(){
            return "https://rickandmortyapi.com/api/character/avatar/$id.jpeg"
        }
}
