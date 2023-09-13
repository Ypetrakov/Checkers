package com.example.checkers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.example.checkers.data.BoardManager
import com.example.checkers.data.BoardManager.board
import com.example.checkers.data.Coord
import com.example.checkers.data.PieceType
import java.util.jar.Attributes

class CheckersBoardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint()
    private val rect = Rect()

    private val darkSquareColor = Color.parseColor("#8D6E63")
    private val lightSquareColor = Color.parseColor("#FFF9C4")

    fun updateBoard(){
        invalidate()
    }
    override fun onDraw(canvas: Canvas) {
        val boardSize = minOf(width, height)
        val margin = 20
        val squareSize = boardSize / 8 - margin / 4
        BoardManager.squareSize = squareSize
        val xOffset = (width - boardSize) / 2 + margin
        val yOffset = (height - boardSize) / 2 + margin

        for (row in 0..7) {
            for (col in 0..7) {
                paint.color = if ((row + col) % 2 == 0) lightSquareColor else darkSquareColor
                val left = xOffset + col * squareSize
                val top = yOffset + row * squareSize
                val right = left + squareSize
                val bottom = top + squareSize
                board[row][col].x = (left+right) / 2
                board[row][col].y = (top+bottom) / 2
                rect.set(left, top, right, bottom)
                canvas.drawRect(rect, paint)
            }
        }
    }
}