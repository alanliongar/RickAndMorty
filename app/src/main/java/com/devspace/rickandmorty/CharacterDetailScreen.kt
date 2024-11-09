package com.devspace.rickandmorty

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
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
    LaunchedEffect(character) {
        color.value = getDominantColorFromImage(context, character.image) ?: Color.Transparent
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
            AdjustableTextSize(character = character, color = color.value)
            Text(
                text = "#${character?.id.toString()?.padStart(3, '0')}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp), color = readableColor(color.value)
            )
            AsyncImage(
                model = character.image,
                contentDescription = "Image of the character ${character.name}",
                modifier = Modifier
                    .height(300.dp)
                    .clip(RoundedCornerShape(64.dp)),
                contentScale = ContentScale.Fit,
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = "Character detail information",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold, color = readableColor(color.value)
            )
            Spacer(modifier = Modifier.height(8.dp))
            CharacterInfo(character = character, color = readableColor(color.value))
        }
    }
}

@Composable
private fun AdjustableTextSize(character: CharacterDetailDto, color: Color) {
    // Tamanho máximo da fonte
    val maxFontSize = 36f // Usando Float para cálculo
    // Definindo um limite máximo de caracteres para reduzir o tamanho da fonte
    val maxLength = 20 // Pode ajustar esse valor dependendo de quanto você quer de limite

    // Calcular o tamanho da fonte baseado no comprimento do nome
    val fontSize = if (character.name.length > maxLength) {
        // Se o nome for maior que o limite, reduz o tamanho da fonte proporcionalmente
        (maxFontSize * (maxLength.toFloat() / character.name.length)).coerceAtMost(maxFontSize)
    } else {
        maxFontSize
    }

    // Convertendo de volta para TextUnit (sp)
    Text(
        text = character.name,
        fontSize = fontSize.sp, // Convertendo o valor para TextUnit (sp)
        color = readableColor(color),
        fontWeight = FontWeight.Bold
    )
}


@Composable
fun CharacterInfo(character: CharacterDetailDto, color: Color) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()) // permite rolar caso o conteúdo exceda a tela
    ) {
        val fontSize = 20.sp
        val space = 8.dp

        InfoRow(
            label = "Status:", value = if (character.status == "") {
                "[Empty]"
            } else {
                character.status
            }, fontSize = fontSize, color = color
        )
        Spacer(modifier = Modifier.height(space))

        InfoRow(
            label = "Species:", value = if (character.species == "") {
                "[Empty]"
            } else {
                character.species
            }, fontSize = fontSize, color = color
        )
        Spacer(modifier = Modifier.height(space))

        InfoRow(
            label = "Type:", value = if (character.type == "") {
                "[Empty]"
            } else {
                character.type
            }, fontSize = fontSize, color = color
        )
        Spacer(modifier = Modifier.height(space))

        InfoRow(
            label = "Gender:", value = if (character.gender == "") {
                "[Empty]"
            } else {
                character.gender
            }, fontSize = fontSize, color = color
        )
        Spacer(modifier = Modifier.height(space))

        InfoRow(
            label = "Origin:", value = if (character.origin.name == "") {
                "[Empty]"
            } else {
                character.origin.name
            }, fontSize = fontSize, color = color
        )
        Spacer(modifier = Modifier.height(space))

        InfoRow(
            label = "Location:", value = if (character.location.name == "") {
                "[Empty]"
            } else {
                character.location.name
            }, fontSize = fontSize, color = color
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String, fontSize: TextUnit, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(100.dp), color = color
        )
        Text(
            text = value,
            fontSize = fontSize,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.Start)
                .wrapContentHeight(Alignment.CenterVertically), color = color
        )
    }
}


