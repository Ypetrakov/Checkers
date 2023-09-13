package com.example.checkers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import com.example.checkers.data.BoardManager.board
import com.example.checkers.data.BoardManager.currentPiece
import com.example.checkers.data.Coord
import com.example.checkers.data.Piece
import com.example.checkers.data.PieceType
import com.example.checkers.data.Tile
import com.example.checkers.databinding.ActivityGameActicityBinding


class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameActicityBinding
    private lateinit var hintViewGroup: FrameLayout
    private lateinit var parentViewGroup: FrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameActicityBinding.inflate(layoutInflater)
        hintViewGroup = FrameLayout(this)
        parentViewGroup = FrameLayout(this)

        binding.root.addView(hintViewGroup)
        binding.root.addView(parentViewGroup)

        setContentView(binding.root)


        binding.button.setOnClickListener {
            currentPiece = null
            for (row in 0..7) {
                for (col in 0..7) {
                    if ((row == 0 || row == 1) && (row + col) % 2 == 1) {
                        board[row][col].piece =
                            Piece(PieceType.Man, false, PieceView(this, row, col))
                    } else if ((row == 6 || row == 7) && (row + col) % 2 == 1) {
                        board[row][col].piece =
                            Piece(PieceType.Man, true, PieceView(this, row, col))
                    } else {
                        board[row][col].piece = null
                    }
                }
            }
            drawPieces()
            it.visibility = View.GONE
            binding.board.updateBoard()
        }

    }

    override fun onDestroy() {
        removePieces()
        removeHints()
        super.onDestroy()
    }

    fun drawPieces() {
        removePieces()
        if(!GameManager.checkIfHaveAvailableMoves()) {
            GameManager.theEnd = true
            GameManager.winner = if (GameManager.teamOneToPlay) "Team 2" else "Team 1"
            Toast.makeText(this, "Win", Toast.LENGTH_LONG).show()
            binding.button.visibility = View.VISIBLE
        }
        for (row in 0..7) {
            for (col in 0..7) {
                val piece = board[row][col].piece
                if (piece != null) {
                    parentViewGroup.addView(piece.view)
                }
            }
        }
        parentViewGroup.invalidate()
    }

    fun drawHints(
        hints: List<Pair<Int, Int>>,
        removePiece: Boolean = false,
        len: Int
    ) {
        removeHints()
        for (hint in hints) {
            hintViewGroup.addView(
                HintView(
                    this,
                    hint.first,
                    hint.second,
                    len,
                    removePiece = removePiece
                )
            )
            Log.d("Hine", hint.toString())
        }
        hintViewGroup.invalidate()

    }

    fun removeHints() {
        hintViewGroup.removeAllViews()
        hintViewGroup.invalidate()
    }

    fun removePieces() {
        parentViewGroup.removeAllViews()
        parentViewGroup.invalidate()
    }


}