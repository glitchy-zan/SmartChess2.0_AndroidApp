package com.example.smartchess20.roomDB.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.smartchess20.roomDB.entities.Game

@Dao
interface GameDao {
    @Insert
    suspend fun insertGame(game: Game)

    @Query("SELECT * FROM game WHERE id = :id")
    suspend fun getGameById(id: Int): Game?

    @Query("SELECT * FROM game")
    suspend fun getAllGames(): MutableList<Game>

    @Update
    suspend fun updateGame(game: Game)

    @Delete
    suspend fun deleteGame(game: Game)
}