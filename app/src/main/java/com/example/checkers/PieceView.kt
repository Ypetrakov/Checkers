package com.example.checkers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.example.checkers.GameManager.teamOneToPlay
import com.example.checkers.data.BoardManager
import com.example.checkers.data.BoardManager.board
import com.example.checkers.data.BoardManager.canBeat
import com.example.checkers.data.BoardManager.currentPiece
import com.example.checkers.data.BoardManager.getPossibleMoves
import com.example.checkers.data.Piece
import com.example.checkers.data.PieceType


class PieceView(context: Context, private var row: Int, private var col: Int) : View(context) {
    private val paint = Paint()
    private var diameter = 0
    private lateinit var piece: Piece


    fun move(row: Int, col: Int) {
        board[this.row][this.col].piece = null
        this.row = row
        this.col = col
        when{
            piece.teamOne && piece.type == PieceType.Man -> if (row == 0) piece.type = PieceType.King
            !piece.teamOne && piece.type == PieceType.Man -> if (row == 7) piece.type = PieceType.King
        }
        board[this.row][this.col].piece = piece
        Log.d("Piece", piece.toString())
        currentPiece = null
        invalidate()
    }
    fun getRow() = row
    fun getCol() = col

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        piece = board[row][col].piece!!
        val parentView = parent as? ViewGroup
        val parentWidth = parentView?.width ?: 0
        val parentHeight = parentView?.height ?: 0
        diameter = minOf(parentWidth, parentHeight) / 8 - 20
        val centerX = board[row][col].x
        val centerY = board[row][col].y
        val radius = diameter / 2 * 0.8
        if (currentPiece == this) {
            paint.color = Color.GREEN
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 10f
            canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), radius.toFloat()+25, paint)
        }
        when {
            piece.type == PieceType.Man && piece.teamOne -> {
                val drawable = AppCompatResources.getDrawable(context, R.drawable.man1) // Replace with your oval shape drawable
                drawable?.setBounds(
                    (centerX - radius).toInt(),
                    (centerY - radius).toInt(),
                    (centerX + radius).toInt(),
                    (centerY + radius).toInt()
                )
                drawable?.draw(canvas)
            }
            piece.type == PieceType.Man && !piece.teamOne -> {

                val drawable = AppCompatResources.getDrawable(context, R.drawable.man2) // Replace with your oval shape drawable
                drawable?.setBounds(
                    (centerX - radius).toInt(),
                    (centerY - radius).toInt(),
                    (centerX + radius).toInt(),
                    (centerY + radius).toInt()
                )
                drawable?.draw(canvas)
            }

            piece.type == PieceType.King && !piece.teamOne -> {
                val drawable = AppCompatResources.getDrawable(context, R.drawable.king1) // Replace with your oval shape drawable
                drawable?.setBounds(
                    (centerX - radius).toInt(),
                    (centerY - radius).toInt(),
                    (centerX + radius).toInt(),
                    (centerY + radius).toInt()
                )
                drawable?.draw(canvas)
            }

            piece.type == PieceType.King && piece.teamOne -> {
                val drawable = AppCompatResources.getDrawable(context, R.drawable.king2) // Replace with your oval shape drawable
                drawable?.setBounds(
                    (centerX - radius).toInt(),
                    (centerY - radius).toInt(),
                    (centerX + radius).toInt(),
                    (centerY + radius).toInt()
                )
                drawable?.draw(canvas)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val touchX = event.x
                val touchY = event.y

                val squareLeft = board[row][col].x.toFloat() - (diameter / 2)
                val squareTop = board[row][col].y.toFloat() - (diameter / 2)
                val squareRight = squareLeft + diameter
                val squareBottom = squareTop + diameter

                if (touchX in squareLeft..squareRight &&
                    touchY >= squareTop && touchY <= squareBottom
                ) {
                    if (piece.teamOne != teamOneToPlay) return true
                    if (currentPiece == this) {
                        currentPiece = null
                        invalidate()
                        (context as GameActivity).removeHints()
                        return true
                    }
                    if (areBeatable()){
                        val beatable = (canBeat(this.row, this.col, if (board[this.row][this.col].piece!!.teamOne) -1 else 1, board[this.row][this.col].piece!!.teamOne))
                        if (beatable.isNotEmpty()) {
                            currentPiece = this
                            (context as GameActivity).drawPieces()
                            (context as GameActivity)
                                .drawHints(
                                    getPossibleMoves(this),
                                    removePiece = true,
                                    len = diameter
                                )
                            return true
                        }
                        else return false
                    }
                    currentPiece = this
                    (context as GameActivity).drawPieces()
                    (context as GameActivity).drawHints(getPossibleMoves(this), len = diameter)
                    return true
                }

            }
        }
        return super.onTouchEvent(event)
    }

    fun areBeatable(): Boolean{
        for(x in 0..7) {
            for (y in 0..7) {
                if (board[x][y].piece != null) {
                    if (board[x][y].piece!!.teamOne == teamOneToPlay) {
                        val direction = if (board[x][y].piece!!.teamOne) -1 else 1
                        if (canBeat(x, y, direction, board[x][y].piece!!.teamOne).isNotEmpty()) return true
                    }
                }
            }
        }
        return false
    }

    fun nextTurn() {
        if(!GameManager.checkIfHaveAvailableMoves()) {
            GameManager.theEnd = true
            GameManager.winner = if (GameManager.teamOneToPlay) "Team 2" else "Team 1"
            Toast.makeText(context, "Win", Toast.LENGTH_LONG).show()
        }
    }

}


