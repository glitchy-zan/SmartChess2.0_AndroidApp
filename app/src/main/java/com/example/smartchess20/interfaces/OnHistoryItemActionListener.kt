package com.example.smartchess20.interfaces

import com.example.smartchess20.roomDB.entities.Game

interface OnHistoryItemActionListener {
    fun onItemUpdated(item: Game)
    fun onItemDeleted(item: Game)
}