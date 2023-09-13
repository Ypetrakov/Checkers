package com.example.checkers

import android.view.View
import com.example.checkers.data.BoardManager.board
import com.example.checkers.data.BoardManager.getPossibleMoves


object GameManager {
    var teamOneToPlay = true
    var hintOpen = false
    var theEnd = false
    var winner = ""

    fun checkIfHaveAvailableMoves(): Boolean {
        for (row in 0..7) {
            for (col in 0..7) {
                if (board[row][col].piece != null) {
                    if (board[row][col].piece!!.view.visibility != View.GONE) {
                        if (board[row][col].piece!!.teamOne == teamOneToPlay) {
                            if (getPossibleMoves(board[row][col].piece!!.view).isNotEmpty()) {
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }
}