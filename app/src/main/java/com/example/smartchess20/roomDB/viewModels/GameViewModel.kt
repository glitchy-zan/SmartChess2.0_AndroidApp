package com.example.smartchess20.roomDB.viewModels

import androidx.lifecycle.ViewModel
import com.example.smartchess20.roomDB.GameDB
import com.example.smartchess20.roomDB.entities.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameViewModel(private val db: GameDB) : ViewModel() {
    suspend fun insertGame(game: Game) {
        withContext(Dispatchers.IO) {
            db.gameDao().insertGame(game)
        }
    }

    suspend fun getGameById(id: Int): Game? {
        return withContext(Dispatchers.IO) {
            db.gameDao().getGameById(id)
        }
    }

    suspend fun getAllGames(): MutableList<Game> {
        return withContext(Dispatchers.IO) {
            db.gameDao().getAllGames()
        }
    }

    suspend fun updateGame(game: Game): MutableList<Game> {
        return withContext(Dispatchers.IO) {
            db.gameDao().updateGame(game)
            db.gameDao().getAllGames()
        }
    }

    suspend fun deleteGame(game: Game): MutableList<Game> {
        return withContext(Dispatchers.IO) {
            db.gameDao().deleteGame(game)
            db.gameDao().getAllGames()
        }
    }
}