package com.example.checkers.data

import android.util.Log
import com.example.checkers.PieceView


object BoardManager {
    var board: Array<Array<Tile>> = Array(8) { Array(8) { Tile() } }
    var currentPiece: PieceView? = null
    var squareSize: Int = 0
    private lateinit var tile: Tile


    fun getPossibleMoves(pieceView: PieceView): List<Pair<Int, Int>> {
        val row = pieceView.getRow()
        val col = pieceView.getCol()
        tile = board[row][col]
        Log.d("current", "row.toString(), col.toString()")

        val direction = if (tile.piece!!.teamOne) -1 else 1

        val beatable = canBeat(row, col, direction, tile.piece!!.teamOne)
        if (beatable.isNotEmpty())
            return beatable

        val possibleMoves = mutableListOf<Pair<Int, Int>>()

        if (tile.piece!!.type == PieceType.King) {
            if (isFree(row + direction, col + direction))
                possibleMoves.add(Pair(row + direction, col + direction))
            if (isFree(row + direction, col - direction))
                possibleMoves.add(Pair(row + direction, col - direction))
            if (isFree(row - direction, col - direction))
                possibleMoves.add(Pair(row - direction, col - direction))
            if (isFree(row - direction, col + direction))
                possibleMoves.add(Pair(row - direction, col + direction))
            return possibleMoves

        }
        if (isFree(row + direction, col + direction))
            possibleMoves.add(Pair(row + direction, col + direction))
        if (isFree(row + direction, col - direction))
            possibleMoves.add(Pair(row + direction, col - direction))

        return possibleMoves
    }


    private fun inBound(x: Int, y: Int): Boolean = (x in 0..7 && y in 0..7)

    private fun isFree(x: Int, y: Int): Boolean {
        if (!inBound(x, y)) return false
        if (board[x][y].piece == null) return true
        return false
    }

    fun canBeat(row: Int, col: Int, direction: Int, teamOne: Boolean): List<Pair<Int, Int>> {
        val listBeatable = mutableListOf<Pair<Int, Int>>()
        if (isEnemy(row + direction, col + direction, teamOne) && isFree(
                row + 2 * direction,
                col + 2 * direction
            )
        ) listBeatable.add(Pair(row + 2 * direction, col + 2 * direction))
        if (isEnemy(row + direction, col - direction, teamOne) && isFree(
                row + 2 * direction,
                col - 2 * direction
            )
        ) listBeatable.add(Pair(row + 2 * direction, col - 2 * direction))

        if (board[row][col].piece!!.type == PieceType.King) {
            val newDirection =  direction * -1
            if (isEnemy(row + newDirection, col + newDirection, teamOne) && isFree(
                    row + 2 * newDirection,
                    col + 2 * newDirection
                )
            ) listBeatable.add(Pair(row + 2 * newDirection, col + 2 * newDirection))
            if (isEnemy(row + newDirection, col - newDirection, teamOne) && isFree(
                    row + 2 * newDirection,
                    col - 2 * newDirection
                )
            ) listBeatable.add(Pair(row + 2 * newDirection, col - 2 * newDirection))
        }
        Log.d("sad", listBeatable.toString())
        return listBeatable
    }

    private fun isEnemy(x: Int, y: Int, teamOne: Boolean): Boolean {
        if (!inBound(x, y)) return false
        if (board[x][y].piece != null)
            if (board[x][y].piece!!.teamOne != teamOne) return true
        return false
    }

}

data class Tile(
    var piece: Piece? = null,
    var x: Int = 0,
    var y: Int = 0
)
