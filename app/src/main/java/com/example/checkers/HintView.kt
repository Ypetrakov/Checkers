package com.example.checkers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.checkers.data.BoardManager
import com.example.checkers.data.BoardManager.board
import com.example.checkers.data.BoardManager.canBeat
import com.example.checkers.data.BoardManager.currentPiece

class HintView(context: Context,
               private val row: Int,
               private val col: Int,
               private val len: Int,
               private val removePiece: Boolean = false,
               private val rowRemove: Int? = null,
               private val colRemove: Int? = null) :
    View(context) {
    private val paint = Paint()

    init {
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val tile = board[row][col]
        canvas.drawCircle(tile.x.toFloat(), tile.y.toFloat(), (len / 6).toFloat(), paint)

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val touchX = event.x
                val touchY = event.y

                val squareLeft = board[row][col].x.toFloat() - (len / 2)
                val squareTop = board[row][col].y.toFloat() - (len / 2)
                val squareRight = squareLeft + len
                val squareBottom = squareTop + len

                if (touchX in squareLeft..squareRight &&
                    touchY >= squareTop && touchY <= squareBottom
                ) {
                    GameManager.teamOneToPlay = !GameManager.teamOneToPlay
                    val oldRow = currentPiece!!.getRow()
                    val oldCol = currentPiece!!.getCol()
                    currentPiece!!.move(row, col)
                    (context as GameActivity).removeHints()

                    if (removePiece) {
                        board[(row + oldRow)/2][(col + oldCol)/2].piece = null
                        (context as GameActivity).drawPieces()
                        val direction = if (board[row][col].piece!!.teamOne) -1 else 1
                        if(canBeat(row, col, direction, board[row][col].piece!!.teamOne).isNotEmpty()){
                            GameManager.teamOneToPlay = !GameManager.teamOneToPlay
                        }
                    }


                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }


}