package com.example.smartchess20.boardManagement

import android.content.Context
import android.widget.GridLayout
import android.widget.ImageView
import com.example.smartchess20.R
import org.json.JSONArray

class ChessBoardManager(context: Context) {
    private val whiteTileColor = context.resources.getColor(R.color.chess_board_white, null)
    private val blackTileColor = context.resources.getColor(R.color.chess_board_black, null)
    private val pieceDrawables = mapOf(
        -6 to R.drawable.king_black,
        -5 to R.drawable.queen_black,
        -4 to R.drawable.rook_black,
        -3 to R.drawable.bishop_black,
        -2 to R.drawable.knight_black,
        -1 to R.drawable.pawn_black,
        1 to R.drawable.pawn_white,
        2 to R.drawable.knight_white,
        3 to R.drawable.bishop_white,
        4 to R.drawable.rook_white,
        5 to R.drawable.queen_white,
        6 to R.drawable.king_white
    )
    private val sensorPieceDrawables = mapOf(
        1 to R.drawable.pawn_white, // Add mappings for other sensors if needed
        0 to 0
    )

    /**
     * Configures the chessboard tiles and sets their background colors.
     */
    fun configureChessboardTiles(
        chessboard: GridLayout,
        chessboardTiles: Array<Array<ImageView>>
    ) {
        for (row in 0 until 8) {
            for (col in 0 until 8) {
                val tile = chessboardTiles[row][col]
                setLayoutParameters(tile, row, col)
                setChessboardTileBackground(tile, row, col)
                chessboard.addView(tile)
            }
        }
    }

    private fun setLayoutParameters(tile: ImageView, row: Int, col: Int) {
        tile.layoutParams = GridLayout.LayoutParams().apply {
            width = 0 // Use weight for distribution
            height = 0
            rowSpec = GridLayout.spec(row, 1f)
            columnSpec = GridLayout.spec(col, 1f)
        }
    }

    private fun setChessboardTileBackground(tile: ImageView, row: Int, col: Int) {
        tile.setBackgroundColor(
            if ((row + col) % 2 == 0) whiteTileColor else blackTileColor
        )
    }

    /**
     * Updates pieces on the chessboard
     */
    fun updateSensorsBoard(
        chessboard: GridLayout,
        chessboardTiles: Array<Array<ImageView>>,
        board: JSONArray
    ) {
        updateBoard(chessboard, chessboardTiles, board, sensorPieceDrawables)
    }

    fun updateChessboard(
        chessboard: GridLayout,
        chessboardTiles: Array<Array<ImageView>>,
        board: JSONArray
    ) {
        updateBoard(chessboard, chessboardTiles, board, pieceDrawables)
    }

    private fun updateBoard(
        chessboard: GridLayout,
        chessboardTiles: Array<Array<ImageView>>,
        board: JSONArray,
        pieceMapping: Map<Int, Int> // Pass the piece mapping
    ) {
        chessboard.removeAllViews()
        for (row in 0 until 8) {
            val rowArray = board.getJSONArray(row)
            for (col in 0 until 8) {
                val tileValue = rowArray.getInt(col)
                val (adjustedRow, adjustedCol) = getAdjustedCoordinates(row, col)
                val tile = chessboardTiles[adjustedRow][adjustedCol]
                tile.setImageResource(pieceMapping[tileValue] ?: 0) // Use 0 for empty tile
                chessboard.addView(tile)
            }
        }
    }

    private fun getAdjustedCoordinates(row: Int, col: Int): Pair<Int, Int> {
        return Pair(7 - row, 7 - col)
    }

    fun clearBoard(
        chessboard: GridLayout,
        chessboardTiles: Array<Array<ImageView>>
    ) {
        chessboard.removeAllViews()
        for (row in 0 until 8) {
            for (col in 0 until 8) {
                val tile = chessboardTiles[row][col]
                tile.setImageResource(0)
                chessboard.addView(tile)
            }
        }
    }
}