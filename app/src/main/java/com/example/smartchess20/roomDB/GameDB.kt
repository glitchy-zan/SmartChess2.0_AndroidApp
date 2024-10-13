package com.example.smartchess20.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smartchess20.roomDB.daos.GameDao
import com.example.smartchess20.roomDB.entities.Game

@Database(entities = [Game::class], version = 1, exportSchema = false)
abstract class GameDB : RoomDatabase() {
    abstract fun gameDao(): GameDao

    /* this makes sure only one instance of db is created */
    companion object {
        @Volatile
        private var INSTANCE: GameDB? = null
        fun getInstance(context: Context): GameDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDB::class.java,
                    "chess_games_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}