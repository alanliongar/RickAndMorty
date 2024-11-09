package com.devspace.rickandmorty.list.presentation.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Size
import com.devspace.rickandmorty.R
import com.devspace.rickandmorty.list.presentation.CharacterListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CharacterListScreen(
    navController: NavHostController,
    viewModel: CharacterListViewModel
) {
    var favoritesFiltered by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val listOfCharacters by viewModel.uiCharacterListUiState.collectAsState()
    val onFavoriteClick: (CharacterUiData) -> Unit = { character ->
        scope.launch(Dispatchers.IO) { viewModel.updateCharacterFavoriteStatus(character) }
    }
    var fabColor by remember { mutableStateOf(Color.Gray) }
    val onFabClick: () -> Unit = {
        if (favoritesFiltered) {
            viewModel.getAllCharacters()
            favoritesFiltered = !favoritesFiltered
            fabColor = Color.Gray
        } else {
            viewModel.getFavoriteCharacters()
            favoritesFiltered = !favoritesFiltered
            fabColor = Color.Yellow
        }
    }
    CharacterListContent(
        fabColor = fabColor,
        listOfCharacters = listOfCharacters,
        viewModel = viewModel,
        onClick = { characterItemClicked ->
            navController.navigate("characterDetail/${characterItemClicked.id}")
        },
        onFavoriteClick = onFavoriteClick,
        onFabClick = onFabClick
    )
}

@Composable
fun SearchAndFilter(viewModel: CharacterListViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSpecies by remember { mutableStateOf("All") }
    Row() {
        TextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                viewModel.fetchFilteredCharacterList(
                    name = query,
                    species = if (selectedSpecies == "All") null else selectedSpecies
                )
            },
            placeholder = {
                Text("Search for a character...")
            }
        )
        SpeciesDropdownMenu(
            selectedSpecies = selectedSpecies,
            onSpeciesSelected = { species ->
                selectedSpecies = species
                viewModel.fetchFilteredCharacterList(
                    name = if (searchQuery == "") null else searchQuery,
                    species = if (species == "All") null else species
                )
            }
        )
    }
}

@Composable
private fun CharacterListContent(
    fabColor: Color,
    listOfCharacters: CharacterListUiState,
    viewModel: CharacterListViewModel,
    onClick: (CharacterUiData) -> Unit,
    onFavoriteClick: (CharacterUiData) -> Unit,
    onFabClick: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClick,
                containerColor = fabColor
            ) {
                Icon(Icons.Default.Star, contentDescription = "Go to Favorites")
            }
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                SearchAndFilter(viewModel = viewModel)

                when {
                    listOfCharacters.isLoading -> {
                        CharacterIsLoading()
                    }

                    listOfCharacters.isError -> {
                        CharacterListErrorUiState(errorMsg = listOfCharacters.errorMessage)
                    }

                    else -> {
                        CharactersGrid(
                            listOfCharacters,
                            onClick = onClick,
                            onFavoriteClick = onFavoriteClick
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun SpeciesDropdownMenu(selectedSpecies: String, onSpeciesSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val speciesOptions =
        listOf(
            "All",
            "Alien",
            "Animal",
            "Cronenberg",
            "Disease",
            "Human",
            "Humanoid",
            "Mythological",
            "Poppybutthole",
            "Robot",
            "unknown"
        )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Specie: $selectedSpecies",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(8.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            speciesOptions.forEach { species ->
                DropdownMenuItem(
                    text = { Text(text = species) },
                    onClick = {
                        onSpeciesSelected(species)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
private fun CharactersGrid(
    listOfCharacters: CharacterListUiState,
    onClick: (CharacterUiData) -> Unit,
    onFavoriteClick: (CharacterUiData) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        items(listOfCharacters.charactersList.size) { index ->
            CharacterCard(
                character = listOfCharacters.charactersList[index],
                onClick = onClick,
                onFavoriteClick = onFavoriteClick
            )
        }
    }
}

@Composable
private fun CharacterCard(
    character: CharacterUiData,
    onClick: (CharacterUiData) -> Unit,
    onFavoriteClick: (CharacterUiData) -> Unit
) {
    val imageUrl: String = character.image
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var color by remember { mutableStateOf(Color.Transparent) }

    LaunchedEffect(imageUrl) {
        isLoading = true
        val dominantColor = getDominantColorFromImage(context, imageUrl)
        color = dominantColor ?: Color.Transparent
        isLoading = false
    }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = color)
            .border(2.dp, color, RoundedCornerShape(16.dp))
            .height(200.dp)
            .clickable { onClick.invoke(character) }
    ) {
        if (isLoading) {
            CharacterIsLoading()
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .build(),
                contentDescription = "Image of ${character.name}",
                modifier = Modifier
                    .width(160.dp)
                    .height(160.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .align(alignment = Alignment.TopCenter),
                contentScale = ContentScale.Fit
            )
        }
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = if (character.isFavorite) "Remover dos favoritos" else "Adicionar aos favoritos",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .width(40.dp)
                .height(40.dp)
                .clickable {
                    onFavoriteClick.invoke(character)
                },
            tint = if (character.isFavorite) Color.Yellow else Color.Gray
        )

        // Nome do personagem
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = character.name,
            fontWeight = FontWeight.Bold,
            color = readableColor(color),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CharacterIsLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            GifImage(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(200.dp))
            )
            Text("Loading...", fontSize = 36.sp)
        }
    }
}

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = R.drawable.loading_image).apply {
                size(Size.ORIGINAL)
            }.build(),
            imageLoader = imageLoader
        ),
        contentDescription = null,
        modifier = modifier
    )
}


@Composable
fun CharacterListErrorUiState(errorMsg: String? = null) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            fontSize = 36.sp,
            modifier = Modifier.padding(16.dp),
            color = Color.Green,
            text = "Ohh No!",
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = painterResource(id = R.drawable.error_image),
            contentDescription = "Error image",
            modifier = Modifier
                .width(450.dp)
                .height(550.dp),
            contentScale = ContentScale.FillHeight
        )
        Text(
            fontSize = 32.sp,
            modifier = Modifier.padding(8.dp),
            color = Color.Green,
            text = "Something went wrong!",
            fontWeight = FontWeight.Bold
        )
        Text(
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp),
            color = Color.Green,
            text = errorMsg ?: "Go back and try again",
            fontWeight = FontWeight.Bold
        )
    }
}

fun readableColor(color: Color): Color {
    val luminance = 0.2126 * color.red + 0.7152 * color.green + 0.0722 * color.blue

    return if (luminance > 0.5) {
        Color.Blue //Azul escuro no caso de uma cor de fundo considerada muito clara
    } else {
        Color(0f, 1f, 0f) //Verde radioativo quando a cor de fundo é escura
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
                val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

                val palette = Palette.from(mutableBitmap).generate()
                val dominantColor =
                    palette.getDominantColor(Color.Black.toArgb()) //Graças à Gabrielle Hallasc
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