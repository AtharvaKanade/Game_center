package com.example.tictactoe

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tictactoe.databinding.ActivityEightPuzzleBinding
import kotlin.random.Random

class EightPuzzleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEightPuzzleBinding
    
    private val puzzleSize = 3
    private val totalTiles = puzzleSize * puzzleSize
    private var puzzle = Array(puzzleSize) { IntArray(puzzleSize) }
    private var emptyRow = puzzleSize - 1
    private var emptyCol = puzzleSize - 1
    private var moveCount = 0
    private var bestMoves = Int.MAX_VALUE
    private var gameCompleted = false
    
    // Goal state: tiles 1-8 in order with empty space at bottom right
    private val goalState = arrayOf(
        intArrayOf(1, 2, 3),
        intArrayOf(4, 5, 6),
        intArrayOf(7, 8, 0) // 0 represents empty space
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEightPuzzleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupClickListeners()
        initializePuzzle()
        createPuzzleGrid()
        updateDisplay()
    }
    
    private fun setupClickListeners() {
        binding.shuffleButton.setOnClickListener { showShuffleConfirmation() }
        binding.newPuzzleButton.setOnClickListener { startNewPuzzle() }
        binding.backToHomeButton.setOnClickListener { finish() }
    }
    
    private fun initializePuzzle() {
        // Start with solved state
        for (row in 0 until puzzleSize) {
            for (col in 0 until puzzleSize) {
                puzzle[row][col] = goalState[row][col]
            }
        }
        emptyRow = puzzleSize - 1
        emptyCol = puzzleSize - 1
        moveCount = 0
        gameCompleted = false
    }
    
    private fun createPuzzleGrid() {
        binding.puzzleGrid.removeAllViews()
        
        for (row in 0 until puzzleSize) {
            for (col in 0 until puzzleSize) {
                val tile = createTile(row, col)
                val params = android.widget.GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    columnSpec = android.widget.GridLayout.spec(col, 1f)
                    rowSpec = android.widget.GridLayout.spec(row, 1f)
                    setMargins(4, 4, 4, 4)
                }
                tile.layoutParams = params
                binding.puzzleGrid.addView(tile)
            }
        }
        
        android.util.Log.d("EightPuzzle", "Puzzle grid created with ${binding.puzzleGrid.childCount} tiles")
    }
    
    private fun createTile(row: Int, col: Int): TextView {
        val tile = TextView(this).apply {
            textSize = 24f
            gravity = Gravity.CENTER
            setTextColor(ContextCompat.getColor(this@EightPuzzleActivity, R.color.on_surface))
            isClickable = true
            isFocusable = true
            setBackgroundResource(R.drawable.tile_background)
            minHeight = 80
            minWidth = 80
            
            setOnClickListener {
                android.util.Log.d("EightPuzzle", "Tile clicked at row: $row, col: $col")
                if (!gameCompleted) {
                    onTileClick(row, col)
                }
            }
        }
        
        updateTileDisplay(tile, row, col)
        return tile
    }
    
    private fun updateTileDisplay(tile: TextView, row: Int, col: Int) {
        val value = puzzle[row][col]
        if (value == 0) {
            tile.text = ""
            tile.setBackgroundResource(R.drawable.empty_tile_background)
        } else {
            tile.text = value.toString()
            tile.setBackgroundResource(R.drawable.tile_background)
            
            // Set consistent brownish color for all tiles
            tile.setBackgroundTintList(ContextCompat.getColorStateList(this@EightPuzzleActivity, R.color.accent))
        }
    }
    
    private fun onTileClick(row: Int, col: Int) {
        android.util.Log.d("EightPuzzle", "onTileClick: row=$row, col=$col, emptyRow=$emptyRow, emptyCol=$emptyCol")
        if (isAdjacentToEmpty(row, col)) {
            android.util.Log.d("EightPuzzle", "Moving tile from ($row, $col) to empty space")
            swapWithEmpty(row, col)
            moveCount++
            updateDisplay()
            checkWinCondition()
            animateTile(binding.puzzleGrid.getChildAt(row * puzzleSize + col))
        } else {
            android.util.Log.d("EightPuzzle", "Tile not adjacent to empty space")
        }
    }
    
    private fun isAdjacentToEmpty(row: Int, col: Int): Boolean {
        return (row == emptyRow && (col == emptyCol - 1 || col == emptyCol + 1)) ||
               (col == emptyCol && (row == emptyRow - 1 || row == emptyRow + 1))
    }
    
    private fun swapWithEmpty(row: Int, col: Int) {
        // Swap the clicked tile with empty space
        puzzle[emptyRow][emptyCol] = puzzle[row][col]
        puzzle[row][col] = 0
        
        // Update empty position
        emptyRow = row
        emptyCol = col
        
        // Update tile displays
        updateAllTiles()
    }
    
    private fun updateAllTiles() {
        for (row in 0 until puzzleSize) {
            for (col in 0 until puzzleSize) {
                val tileIndex = row * puzzleSize + col
                val tile = binding.puzzleGrid.getChildAt(tileIndex) as TextView
                updateTileDisplay(tile, row, col)
            }
        }
    }
    
    private fun shufflePuzzle() {
        // Generate a solvable random puzzle
        do {
            generateRandomPuzzle()
        } while (!isSolvable())
        
        moveCount = 0
        gameCompleted = false
        updateAllTiles()
        updateDisplay()
    }
    
    private fun generateRandomPuzzle() {
        // Create a list of numbers 0-8
        val numbers = (0..8).toMutableList()
        numbers.shuffle()
        
        // Fill the puzzle grid
        var index = 0
        for (row in 0 until puzzleSize) {
            for (col in 0 until puzzleSize) {
                puzzle[row][col] = numbers[index++]
                if (numbers[index - 1] == 0) {
                    emptyRow = row
                    emptyCol = col
                }
            }
        }
    }
    
    private fun isSolvable(): Boolean {
        // Count inversions (pairs of tiles that are out of order)
        var inversions = 0
        val flatPuzzle = mutableListOf<Int>()
        
        // Flatten the 2D array manually
        for (row in puzzle) {
            for (value in row) {
                if (value != 0) {
                    flatPuzzle.add(value)
                }
            }
        }
        
        for (i in flatPuzzle.indices) {
            for (j in i + 1 until flatPuzzle.size) {
                if (flatPuzzle[i] > flatPuzzle[j]) {
                    inversions++
                }
            }
        }
        
        // For 3x3 puzzle: solvable if inversions is even
        return inversions % 2 == 0
    }
    

    
    private fun checkWinCondition() {
        if (isPuzzleSolved()) {
            gameCompleted = true
            if (moveCount < bestMoves) {
                bestMoves = moveCount
            }
            showCompletionDialog()
        }
    }
    
    private fun isPuzzleSolved(): Boolean {
        for (row in 0 until puzzleSize) {
            for (col in 0 until puzzleSize) {
                if (puzzle[row][col] != goalState[row][col]) {
                    return false
                }
            }
        }
        return true
    }
    
    private fun showCompletionDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.puzzle_completed))
            .setMessage(getString(R.string.you_completed, moveCount))
            .setPositiveButton(getString(R.string.try_another)) { _, _ ->
                startNewPuzzle()
            }
            .setNegativeButton(getString(R.string.back_to_home)) { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }
    
    private fun showShuffleConfirmation() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.shuffle_confirm))
            .setMessage(getString(R.string.shuffle_message))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                shufflePuzzle()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }
    
    private fun startNewPuzzle() {
        initializePuzzle()
        updateAllTiles()
        updateDisplay()
    }
    
    private fun updateDisplay() {
        binding.movesText.text = getString(R.string.moves, moveCount)
        binding.bestMovesText.text = getString(R.string.best_moves, bestMoves)
    }
    
    private fun animateTile(view: View) {
        val animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        animation.duration = 150
        view.startAnimation(animation)
    }
}
