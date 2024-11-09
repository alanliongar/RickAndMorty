package com.devspace.rickandmorty

import android.app.Application
import androidx.room.Room
import com.devspace.rickandmorty.common.remote.RetrofitClient
import com.devspace.rickandmorty.common.local.AppDataBase
import com.devspace.rickandmorty.list.data.CharacterListRepository
import com.devspace.rickandmorty.list.data.local.CharacterListLocalDataSource
import com.devspace.rickandmorty.list.data.remote.CharacterListRemoteDataSource
import com.devspace.rickandmorty.list.data.remote.CharacterListService

class CharacterApplication() : Application() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java,
            "characters-database"
        ).build()
    }
    private val characterListService by lazy {
        RetrofitClient.retrofitInstance.create(CharacterListService::class.java)
    }
    private val characterLocalDataSource: CharacterListLocalDataSource by lazy {
        CharacterListLocalDataSource(db.getCharacterDao())
    }
    private val characterRemoteDataSource: CharacterListRemoteDataSource by lazy {
        CharacterListRemoteDataSource(characterListService)
    }
    val repository: CharacterListRepository by lazy {
        CharacterListRepository(
            local = characterLocalDataSource,
            remote = characterRemoteDataSource
        )
    }
}