package com.example.checkers.data

import com.example.checkers.PieceView

enum class PieceType{
    Man,
    King
}
data class Piece(
    var type: PieceType,
    var teamOne: Boolean,
    var view: PieceView
)
