package com.devspace.rickandmorty.common.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characterlistentity WHERE isFavorite = 1")
    suspend fun getFavoriteCharacters(): List<CharacterListEntity>

    @Query(//Essa query busca o nome parcial ou completo, com a espécie no roomdatabase.
        """
        SELECT * FROM characterlistentity 
        WHERE (:name IS NULL OR name LIKE '%' || :name || '%')
        AND (:specie IS NULL OR specie = :specie)
        """
    )
    suspend fun getFilteredCharacters(
        name: String? = null,
        specie: String? = null
    ): List<CharacterListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(charactersList: List<CharacterListEntity>)

    @Update
    suspend fun updateCharacter(character: CharacterListEntity)

    @Query("SELECT * FROM characterlistentity WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterListEntity
}