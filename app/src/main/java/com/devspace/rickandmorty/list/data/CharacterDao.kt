package com.devspace.rickandmorty.list.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterDao {

    @Query("Select * From characterentity")
    suspend fun getAll(category: String): List<CharacterEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<CharacterEntity>)

}