package com.devspace.rickandmorty

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devspace.rickandmorty.detail.presentation.CharacterDetailViewModel
import com.devspace.rickandmorty.detail.presentation.ui.CharacterDetailScreen
import com.devspace.rickandmorty.list.presentation.CharacterListViewModel
import com.devspace.rickandmorty.list.presentation.ui.CharacterListScreen


@Composable
fun RickAndMortyApp(characterListViewModel: CharacterListViewModel, characterDetailViewModel: CharacterDetailViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "characterList") {
        composable("characterList") {
            CharacterListScreen(navController = navController, viewModel = characterListViewModel)
        }
        composable(
            route = "characterDetail/{characterId}",
            arguments = listOf(navArgument("characterId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val characterId = requireNotNull(backStackEntry.arguments?.getString("characterId"))
            CharacterDetailScreen(characterId = characterId, viewModel = characterDetailViewModel)
        }
        composable("favorites") {

        }
    }
}
