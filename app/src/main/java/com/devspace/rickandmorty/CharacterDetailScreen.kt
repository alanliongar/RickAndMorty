package com.devspace.rickandmorty

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope

@Composable
fun CharacterDetailScreen(characterId: String) {
    val characterApiService =
        RetrofitClient.retrofitInstance.create(CharactersServices::class.java)
    var characterDetail = remember { mutableStateOf<CharacterDetailDto?>(null) }
    LaunchedEffect(characterDetail.value) {
        try {
            val response = characterApiService.getCharacterById(characterId.toInt())
            characterDetail.value = response
            Log.d("CharacterDetailScreen", "Success")
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.d("CharacterDetailScreen", ex.message.toString())
            characterDetail.value = null
        }
    }
    characterDetail.value?.let { CharacterDetailContent(character = it) }
}

@Composable
fun CharacterDetailContent(character: CharacterDetailDto, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    LaunchedEffect(character) {
        val color = getDominantColorFromImage(context, character.image)?: Color.Transparent
        val fontColor = invertColor(color)
    }






}