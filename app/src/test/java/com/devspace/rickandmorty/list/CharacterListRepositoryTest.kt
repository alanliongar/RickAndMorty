package com.devspace.rickandmorty.list

import android.accounts.NetworkErrorException
import com.devspace.rickandmorty.common.model.Character
import com.devspace.rickandmorty.list.data.CharacterListRepository
import com.nhaarman.mockitokotlin2.mock
import com.devspace.rickandmorty.list.data.local.CharacterListLocalDataSource
import com.devspace.rickandmorty.list.data.remote.CharacterListRemoteDataSource
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CharacterListRepositoryTest {
    private val local: CharacterListLocalDataSource = mock()
    private val remote: CharacterListRemoteDataSource = mock()

    private val underTest by lazy {
        CharacterListRepository(local = local, remote = remote)
    }


    @Test
    fun `Given no internet connection When getting character list Then return local data`() {
        runTest {
            //Given
            val localList = listOf(
                Character(
                    id = 1,
                    name = "Rick Sanchez",
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    specie = "Human",
                    isFavorite = false
                )
            )
            whenever(remote.getFilteredCharacters()).thenReturn(Result.failure(NetworkErrorException()))
            whenever(local.getCharacterList()).thenReturn(localList)

            //Then

            //When
        }
    }
}