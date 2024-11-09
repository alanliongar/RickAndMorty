package com.devspace.rickandmorty.list

import com.devspace.rickandmorty.list.data.CharacterListRepository
import com.nhaarman.mockitokotlin2.mock
import com.devspace.rickandmorty.list.data.local.CharacterListLocalDataSource
import com.devspace.rickandmorty.list.data.remote.CharacterListRemoteDataSource

class CharacterListRepositoryTest {
    private val local: CharacterListLocalDataSource = mock()
    private val remote: CharacterListRemoteDataSource = mock()

    private val underTest by lazy {
        CharacterListRepository(local = local, remote = remote)
    }
}