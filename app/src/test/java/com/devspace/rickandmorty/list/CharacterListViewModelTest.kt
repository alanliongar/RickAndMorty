package com.devspace.rickandmorty.list

import com.devspace.rickandmorty.common.model.Character
import com.devspace.rickandmorty.list.data.CharacterListRepository
import com.devspace.rickandmorty.list.presentation.CharacterListViewModel
import com.devspace.rickandmorty.list.presentation.ui.CharacterListUiState
import com.devspace.rickandmorty.list.presentation.ui.CharacterUiData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CharacterListViewModelTest {

    private val repository: CharacterListRepository = mock()
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher(TestCoroutineScheduler())

    private val underTest by lazy {
        CharacterListViewModel(repository = repository, dispatcher = testDispatcher)
    }

    @Test
    fun `Given fresh viewModel when collecting to uiListState Then assert expected list`() {
        runTest {
            //Given
            val charactersList = listOf(
                Character(
                    id = 1,
                    name = "Rick Sanchez",
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    specie = "Human"
                )
            )
            whenever(repository.getFilteredCharacters()).thenReturn(Result.success(charactersList))

            //When
            val result = underTest.uiCharacterListUiState.take(2).toList()[1]

            //Then assert expected value
            val expected = CharacterListUiState(
                charactersList = listOf(
                    CharacterUiData(
                        id = 1,
                        name = "Rick Sanchez",
                        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                        specie = "Human"
                    )
                )
            )
            assertEquals(expected, result)
        }
    }

    @Test
    fun `Given fresh viewModel when collecting to uiListState Then assert loading state`() {
        runTest {
            //Given
            val charactersList = listOf(
                Character( //aqui tanto faz colocar esse cara ou não, pq o loading é definido mesmo assim.
                    id = 1,
                    name = "Rick Sanchez",
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    specie = "Human"
                )
            )
            whenever(repository.getFilteredCharacters()).thenReturn(Result.success(charactersList))

            //When
            val result = underTest.uiCharacterListUiState.value

            //Then assert expected value
            val expected = CharacterListUiState(
                isLoading = true
            )
            assertEquals(expected, result)
        }
    }
}