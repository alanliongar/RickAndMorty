package com.devspace.rickandmorty.list.presentation.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.devspace.rickandmorty.list.model.CharacterDto
import com.devspace.rickandmorty.list.model.CharacterListResponse
import com.devspace.rickandmorty.common.RetrofitClient
import com.devspace.rickandmorty.list.data.remote.CharacterListServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun CharacterListScreen(navController: NavHostController) {
    val characterApiService =
        RetrofitClient.retrofitInstance.create(CharacterListServices::class.java)
    var listOfCharacters = remember { mutableStateOf<CharacterListResponse?>(null) }
    LaunchedEffect(listOfCharacters.value) {
        try {
            val response = characterApiService.getAllCharacters()
            if (response.results.isNotEmpty()) {
                listOfCharacters.value = response
                Log.d("CharacterListScreen", "Success")
            } else {
                listOfCharacters.value = null
                Log.d("CharacterListScreen", "EmptyCall")
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.d("CharacterListScreen", ex.message.toString())
            listOfCharacters.value = null
        }
    }
    CharacterListContent(listOfCharacters = listOfCharacters) { characterItemClicked ->
        navController.navigate("characterDetail/${characterItemClicked.id}")
    }
}

@Composable
private fun CharacterListContent(listOfCharacters: MutableState<CharacterListResponse?>, onClick: (CharacterDto) -> Unit) {
    if (listOfCharacters.value == null) {
        Text("Carregando....")
    } else {
        CharactersGrid(listOfCharacters.value, onClick = onClick)
    }
}


@Composable
private fun CharactersGrid(
    listOfCharacters: CharacterListResponse?,
    onClick: (CharacterDto) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        if (listOfCharacters != null) {
            items(listOfCharacters.results.size) { index ->
                CharacterCard(character = listOfCharacters.results[index], onClick = onClick)
            }
        }
    }
}

@Composable
private fun CharacterCard(character: CharacterDto, onClick: (CharacterDto) -> Unit) {
    val imageUrl: String = character.imageUrl
    val context = LocalContext.current
    var color by remember { mutableStateOf(Color.Transparent) }
    LaunchedEffect(imageUrl) {
        val dominantColor = getDominantColorFromImage(context, imageUrl)
        color = dominantColor ?: Color.Transparent
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = color)
            .border(2.dp, color, RoundedCornerShape(16.dp))
            .height(200.dp)
            .clickable { onClick.invoke(character) },
        // Cor de fundo baseada no personagem
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .build(),
            contentDescription = "Image of ${character.name}",
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = character.name,
            fontWeight = FontWeight.Bold,
            color = readableColor(color),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            overflow = TextOverflow.Ellipsis
        )
    }
}

fun readableColor(color: Color): Color {
    val luminance = 0.2126 * color.red + 0.7152 * color.green + 0.0722 * color.blue

    return if (luminance > 0.5) { //muito claro
        Color.Black  // Preto, no caso de uma cor de fundo considerada muito clara
    } else {
        Color(0f, 1f, 0f)  // Verde vibrante, escolha de paleta
    }
}

suspend fun getDominantColorFromImage(
    context: Context,
    imageUrl: String?
): Color? {
    return withContext(Dispatchers.IO) {
        val imageLoader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .build()
        return@withContext try {
            val result = (imageLoader.execute(request) as SuccessResult).drawable
            if (result is BitmapDrawable) {
                val bitmap = result.bitmap
                // Verifica se o bitmap é do tipo HARDWARE e converte se necessário
                val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

                val palette = Palette.from(mutableBitmap).generate()
                val dominantColor =
                    palette.getDominantColor(androidx.compose.ui.graphics.Color.Black.toArgb())
                Color(dominantColor).copy(alpha = 0.70f)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Erro ao obter cor da imagem: ${e.message}")
            null
        }
    }
}