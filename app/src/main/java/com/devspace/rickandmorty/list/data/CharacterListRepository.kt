package com.devspace.rickandmorty.list.data

import android.accounts.NetworkErrorException
import com.devspace.rickandmorty.list.data.local.CharacterListLocalDataSource
import com.devspace.rickandmorty.list.data.remote.CharacterListRemoteDataSource
import com.devspace.rickandmorty.list.data.remote.CharacterListService
import com.devspace.rickandmorty.common.remote.model.CharacterListResponse

class CharacterListRepository(
    private val local: CharacterListLocalDataSource,
    private val remote: CharacterListRemoteDataSource,
) {

}