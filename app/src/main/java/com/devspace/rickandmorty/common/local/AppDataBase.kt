package com.devspace.rickandmorty.common.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database([CharacterListEntity::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun getCharacterDao(): CharacterDao
}