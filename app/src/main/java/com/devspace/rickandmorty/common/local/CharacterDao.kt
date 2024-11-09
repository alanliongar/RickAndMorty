package com.devspace.rickandmorty.common.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterDao {

    @Query("Select * From characterlistentity")
    suspend fun getAll(): List<CharacterListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(charactersList: List<CharacterListEntity>)
}