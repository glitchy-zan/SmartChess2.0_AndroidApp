package com.example.smartchess20.roomDB.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game")
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo
    var pgn: String,
    @ColumnInfo
    var white_player: String,
    @ColumnInfo
    var black_player: String,
)