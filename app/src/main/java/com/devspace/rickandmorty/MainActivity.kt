package com.devspace.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.devspace.rickandmorty.detail.presentation.CharacterDetailViewModel
import com.devspace.rickandmorty.list.presentation.CharacterListViewModel
import com.devspace.rickandmorty.ui.theme.RickAndMortytheme

class MainActivity : ComponentActivity() {

    private val characterListViewModel by viewModels<CharacterListViewModel> {CharacterListViewModel.Factory}
    private val characterDetailViewModel by viewModels<CharacterDetailViewModel> {CharacterDetailViewModel.Factory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RickAndMortytheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RickAndMortyApp(characterListViewModel = characterListViewModel, characterDetailViewModel = characterDetailViewModel)
                }
            }
        }
    }
}

