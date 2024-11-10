package com.devspace.rickandmorty.list

import android.accounts.NetworkErrorException
import com.devspace.rickandmorty.common.model.Character
import com.devspace.rickandmorty.list.data.CharacterListRepository
import com.nhaarman.mockitokotlin2.mock
import com.devspace.rickandmorty.list.data.local.CharacterListLocalDataSource
import com.devspace.rickandmorty.list.data.remote.CharacterListRemoteDataSource
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.net.UnknownHostException

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
            val result = underTest.getFilteredCharacters()

            //When
            val expected = Result.success(localList)
            assertEquals(expected, result)
        }
    }

    @Test
    fun `Given no internet connection and no local data When getting character list Then return result`() {
        runTest {
            //Given
            val remoteResult = Result.failure<List<Character>?>(UnknownHostException("No internet connection"))
            whenever(remote.getFilteredCharacters()).thenReturn(remoteResult)
            whenever(local.getCharacterList()).thenReturn(emptyList())

            //Then
            val result = underTest.getFilteredCharacters()

            //When
            val expected = remoteResult
            assertEquals(expected, result)
        }
    }

    @Test
    fun `Given remote success When getting character list Then update local data`() {
        runTest {
            //Given
            val list = listOf(
                Character(
                    id = 1,
                    name = "Rick Sanchez",
                    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                    specie = "Human",
                    isFavorite = false
                )
            )
            val remoteResult = Result.success(list)
            whenever(remote.getFilteredCharacters()).thenReturn(remoteResult)
            whenever(local.getCharacterList()).thenReturn(list)

            //Then
            val result = underTest.getFilteredCharacters()

            //When
            val expected = Result.success(list)
            assertEquals(expected, result)
            verify(local).updateCharacterList(list)
        }
    }
}