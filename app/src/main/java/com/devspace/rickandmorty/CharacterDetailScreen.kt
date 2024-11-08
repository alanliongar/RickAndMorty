package com.devspace.rickandmorty

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

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
    val color = remember { mutableStateOf(Color.Transparent) }
    val fontColor = remember{mutableStateOf(invertColor(color.value))}
    LaunchedEffect(character) {
        color.value = getDominantColorFromImage(context, character.image)?: Color.Transparent
        val fontColor = invertColor(color.value)
    }

    Box(
        modifier = Modifier
            .background(color.value)
            .fillMaxSize()
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = character.name,
                fontSize = 36.sp,
                color = fontColor.value,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "#${character?.id.toString()?.padStart(3, '0')}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
                AsyncImage(
                    model = character.image,
                    contentDescription = "Image of the character ${character.name}",
                    modifier = Modifier.height(300.dp), contentScale = ContentScale.Fit
                )
            Spacer(modifier = Modifier.size(6.dp))
            Text(text = "Character detail information", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(){
                Column(){
                    Text(text = "Status:", fontSize = 14.sp)
                    Text(text = "Species:", fontSize = 14.sp)
                    Text(text = "Type:", fontSize = 14.sp)
                    Text(text = "Gender:", fontSize = 14.sp)
                    Text(text = "Origin:", fontSize = 14.sp)
                    Text(text = "Location:", fontSize = 14.sp)
                }
                Column(){
                    Text(text = "${character.status}", fontSize = 14.sp)
                    Text(text = "${character.species}", fontSize = 14.sp)
                    Text(text = "${character.type}", fontSize = 14.sp)
                    Text(text = "${character.gender}", fontSize = 14.sp)
                    Text(text = "${character.origin.name}", fontSize = 14.sp)
                    Text(text = "${character.location.name}", fontSize = 14.sp)
                }
            }
        }
    }

}