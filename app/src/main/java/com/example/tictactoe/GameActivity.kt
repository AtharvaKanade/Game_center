package com.example.tictactoe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.GridLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.databinding.ActivityGameBinding
import com.example.tictactoe.databinding.ItemCellBinding
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private lateinit var gameMode: GameMode
    private lateinit var gameBoard: Array<Array<Player?>>
    private var currentPlayer: Player = Player.X
    private var gameEnded = false
    private var isComputerThinking = false
    private val cells = mutableListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityGameBinding.inflate(layoutInflater)
            setContentView(binding.root)

            gameMode = GameMode.valueOf(intent.getStringExtra("GAME_MODE") ?: GameMode.VS_FRIEND.name)
            initializeGame()
            setupClickListeners()
        } catch (e: Exception) {
            android.util.Log.e("GameActivity", "Error in onCreate: ${e.message}", e)
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Failed to initialize game: ${e.message}")
                .setPositiveButton("OK") { _, _ -> finish() }
                .setCancelable(false)
                .show()
        }
    }

    private fun initializeGame() {
        gameBoard = Array(3) { Array(3) { null } }
        currentPlayer = Player.X
        gameEnded = false
        isComputerThinking = false
        cells.clear()
        binding.gameBoard.removeAllViews()
        createGameBoard()
        updateStatus()
        updateCellInteractivity() // Ensure initial cell state is correct
    }

    private fun createGameBoard() {
        try {
            android.util.Log.d("GameActivity", "Creating game board...")
            
            // Clear existing cells
            binding.gameBoard.removeAllViews()
            cells.clear()
            
            for (row in 0..2) {
                for (col in 0..2) {
                    val cellBinding = ItemCellBinding.inflate(LayoutInflater.from(this))
                    val cell = cellBinding.root
                    cell.tag = Pair(row, col)
                    cell.setOnClickListener { onCellClick(row, col) }
                    
                    // Set GridLayout parameters for proper positioning
                    val params = GridLayout.LayoutParams().apply {
                        width = 0
                        height = 0
                        columnSpec = GridLayout.spec(col, 1f)
                        rowSpec = GridLayout.spec(row, 1f)
                        setMargins(1, 1, 1, 1)
                    }
                    cell.layoutParams = params
                    
                    cells.add(cell)
                    binding.gameBoard.addView(cell)
                    
                    android.util.Log.d("GameActivity", "Added cell at row $row, col $col")
                }
            }
            
            android.util.Log.d("GameActivity", "Game board created with ${cells.size} cells")
        } catch (e: Exception) {
            android.util.Log.e("GameActivity", "Error creating game board: ${e.message}", e)
            throw e
        }
    }

    private fun onCellClick(row: Int, col: Int) {
        android.util.Log.d("GameActivity", "Cell clicked at row $row, col $col")
        
        if (gameEnded || gameBoard[row][col] != null || isComputerThinking) {
            android.util.Log.d("GameActivity", "Cell already occupied, game ended, or computer is thinking")
            return
        }

        android.util.Log.d("GameActivity", "Making move for player ${currentPlayer.symbol}")
        makeMove(row, col, currentPlayer)

        if (checkWinner(row, col)) {
            android.util.Log.d("GameActivity", "Winner found: ${currentPlayer.symbol}")
            endGame(currentPlayer)
            return
        }

        if (isBoardFull()) {
            android.util.Log.d("GameActivity", "Game is a draw")
            endGame(null)
            return
        }

        switchPlayer()

        if (gameMode == GameMode.VS_COMPUTER && currentPlayer == Player.O) {
            android.util.Log.d("GameActivity", "Computer's turn")
            // Block user input during computer's turn
            isComputerThinking = true
            binding.currentPlayerText.text = getString(R.string.computer_thinking)
            updateCellInteractivity() // Update visual state immediately
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                makeComputerMove()
            }, 800) // 800ms delay
        }
    }

    private fun makeMove(row: Int, col: Int, player: Player) {
        android.util.Log.d("GameActivity", "Making move: ${player.symbol} at ($row, $col)")
        
        gameBoard[row][col] = player
        val cellIndex = row * 3 + col
        
        if (cellIndex < cells.size) {
            val cell = cells[cellIndex]
            
            // Add animation for the move
            cell.alpha = 0f
            cell.scaleX = 0.5f
            cell.scaleY = 0.5f
            
            // Animate the symbol appearance
            cell.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .setInterpolator(android.view.animation.DecelerateInterpolator())
                .start()
            
            // Set the text and color
            cell.text = player.symbol
            cell.setTextColor(getPlayerColor(player))
            
            android.util.Log.d("GameActivity", "Move displayed on cell $cellIndex with animation")
        } else {
            android.util.Log.e("GameActivity", "Cell index $cellIndex out of bounds. Cells size: ${cells.size}")
        }
    }

    private fun makeComputerMove() {
        // Advanced AI: find best move using minimax algorithm
        val bestMove = findBestMove()
        if (bestMove != null) {
            android.util.Log.d("GameActivity", "Computer making optimal move at (${bestMove.first}, ${bestMove.second})")
            makeMove(bestMove.first, bestMove.second, Player.O)
        } else {
            // Fallback: make random move (should never happen with proper minimax)
            val availableMoves = mutableListOf<Pair<Int, Int>>()
            for (row in 0..2) {
                for (col in 0..2) {
                    if (gameBoard[row][col] == null) {
                        availableMoves.add(Pair(row, col))
                    }
                }
            }
            if (availableMoves.isNotEmpty()) {
                val randomMove = availableMoves.random()
                android.util.Log.w("GameActivity", "Falling back to random move at (${randomMove.first}, ${randomMove.second})")
                makeMove(randomMove.first, randomMove.second, Player.O)
            }
        }

        if (checkWinnerForPlayer(currentPlayer)) {
            endGame(currentPlayer)
            return
        }

        if (isBoardFull()) {
            endGame(null)
            return
        }

        switchPlayer()
        // Re-enable user input after computer's turn
        isComputerThinking = false
        updateCellInteractivity() // Update visual state immediately
    }

    private fun findBestMove(): Pair<Int, Int>? {
        // Quick win check - if computer can win in one move, do it
        for (row in 0..2) {
            for (col in 0..2) {
                if (gameBoard[row][col] == null) {
                    gameBoard[row][col] = Player.O
                    if (checkWinner(row, col)) {
                        gameBoard[row][col] = null
                        android.util.Log.d("GameActivity", "Found winning move at (${row}, ${col})")
                        return Pair(row, col)
                    }
                    gameBoard[row][col] = null
                }
            }
        }
        
        // Quick block check - if player can win in one move, block it
        for (row in 0..2) {
            for (col in 0..2) {
                if (gameBoard[row][col] == null) {
                    gameBoard[row][col] = Player.X
                    if (checkWinner(row, col)) {
                        gameBoard[row][col] = null
                        android.util.Log.d("GameActivity", "Found blocking move at (${row}, ${col})")
                        return Pair(row, col)
                    }
                    gameBoard[row][col] = null
                }
            }
        }
        
        // Use minimax algorithm for optimal play
        var bestScore = Int.MIN_VALUE
        var bestMove: Pair<Int, Int>? = null
        
        // Get all available moves
        val availableMoves = mutableListOf<Pair<Int, Int>>()
        for (row in 0..2) {
            for (col in 0..2) {
                if (gameBoard[row][col] == null) {
                    availableMoves.add(Pair(row, col))
                }
            }
        }
        
        // If no moves available, return null
        if (availableMoves.isEmpty()) return null
        
        // For first few moves, use strategic positions
        if (availableMoves.size >= 7) {
            // First move - prefer center
            if (gameBoard[1][1] == null) {
                android.util.Log.d("GameActivity", "Strategic move: taking center")
                return Pair(1, 1)
            }
            
            // Second move - prefer corners if center is taken
            if (gameBoard[1][1] == Player.X) {
                val corners = listOf(Pair(0, 0), Pair(0, 2), Pair(2, 0), Pair(2, 2))
                val availableCorners = corners.filter { gameBoard[it.first][it.second] == null }
                if (availableCorners.isNotEmpty()) {
                    android.util.Log.d("GameActivity", "Strategic move: taking corner at (${availableCorners.first().first}, ${availableCorners.first().second})")
                    return availableCorners.first() // Don't randomize, be consistent
                }
            }
        }
        
        // Special case: If player has diagonal X's and computer is in center, 
        // avoid placing in the remaining corner to prevent fork
        if (gameBoard[1][1] == Player.O) {
            val diagonalXCount = (if (gameBoard[0][0] == Player.X) 1 else 0) + 
                                (if (gameBoard[2][2] == Player.X) 1 else 0) +
                                (if (gameBoard[0][2] == Player.X) 1 else 0) + 
                                (if (gameBoard[2][0] == Player.X) 1 else 0)
            
            if (diagonalXCount >= 2) {
                // Prefer edge positions over corners to prevent fork
                val edgePositions = listOf(Pair(0, 1), Pair(1, 0), Pair(1, 2), Pair(2, 1))
                val availableEdges = edgePositions.filter { gameBoard[it.first][it.second] == null }
                if (availableEdges.isNotEmpty()) {
                    android.util.Log.d("GameActivity", "Fork prevention: choosing edge at (${availableEdges.first().first}, ${availableEdges.first().second}) instead of corner")
                    return availableEdges.first()
                }
            }
        }
        
        // Evaluate each move using minimax
        android.util.Log.d("GameActivity", "Evaluating ${availableMoves.size} moves using minimax algorithm...")
        for (move in availableMoves) {
            gameBoard[move.first][move.second] = Player.O
            val score = minimax(gameBoard, 0, false, Int.MIN_VALUE, Int.MAX_VALUE)
            gameBoard[move.first][move.second] = null
            
            android.util.Log.d("GameActivity", "Move (${move.first}, ${move.second}) has score: $score")
            
            if (score > bestScore) {
                bestScore = score
                bestMove = move
                android.util.Log.d("GameActivity", "New best move: (${move.first}, ${move.second}) with score: $score")
            }
        }
        
        android.util.Log.d("GameActivity", "Final best move: (${bestMove?.first}, ${bestMove?.second}) with score: $bestScore")
        return bestMove
    }
    
    private fun minimax(board: Array<Array<Player?>>, depth: Int, isMaximizing: Boolean, alpha: Int, beta: Int): Int {
        // Check for terminal states
        if (checkWinnerForPlayerOnBoard(board, Player.O)) return 10 - depth
        if (checkWinnerForPlayerOnBoard(board, Player.X)) return depth - 10
        if (isBoardFull(board)) return 0
        
        // Get available moves
        val availableMoves = mutableListOf<Pair<Int, Int>>()
        for (row in 0..2) {
            for (col in 0..2) {
                if (board[row][col] == null) {
                    availableMoves.add(Pair(row, col))
                }
            }
        }
        
        if (isMaximizing) {
            var maxScore = Int.MIN_VALUE
            var currentAlpha = alpha
            for (move in availableMoves) {
                board[move.first][move.second] = Player.O
                val score = minimax(board, depth + 1, false, currentAlpha, beta)
                board[move.first][move.second] = null
                maxScore = maxOf(maxScore, score)
                
                // Alpha-beta pruning
                currentAlpha = maxOf(currentAlpha, score)
                if (beta <= currentAlpha) break
            }
            return maxScore
        } else {
            var minScore = Int.MAX_VALUE
            var currentBeta = beta
            for (move in availableMoves) {
                board[move.first][move.second] = Player.X
                val score = minimax(board, depth + 1, true, alpha, currentBeta)
                board[move.first][move.second] = null
                minScore = minOf(minScore, score)
                
                // Alpha-beta pruning
                currentBeta = minOf(currentBeta, score)
                if (currentBeta <= alpha) break
            }
            return minScore
        }
    }
    
    private fun checkWinnerForPlayerOnBoard(board: Array<Array<Player?>>, player: Player): Boolean {
        // Check rows
        for (row in 0..2) {
            if (board[row][0] == player && board[row][1] == player && board[row][2] == player) {
                return true
            }
        }
        
        // Check columns
        for (col in 0..2) {
            if (board[0][col] == player && board[1][col] == player && board[2][col] == player) {
                return true
            }
        }
        
        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true
        }
        
        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true
        }
        
        return false
    }
    
    private fun isBoardFull(board: Array<Array<Player?>>): Boolean {
        for (row in 0..2) {
            for (col in 0..2) {
                if (board[row][col] == null) return false
            }
        }
        return true
    }

    private fun checkWinner(row: Int, col: Int): Boolean {
        val player = gameBoard[row][col] ?: return false
        
        // Check row
        if (gameBoard[row][0] == player && gameBoard[row][1] == player && gameBoard[row][2] == player) {
            return true
        }
        
        // Check column
        if (gameBoard[0][col] == player && gameBoard[1][col] == player && gameBoard[2][col] == player) {
            return true
        }
        
        // Check diagonals
        if (row == col && gameBoard[0][0] == player && gameBoard[1][1] == player && gameBoard[2][2] == player) {
            return true
        }
        
        if (row + col == 2 && gameBoard[0][2] == player && gameBoard[1][1] == player && gameBoard[2][0] == player) {
            return true
        }
        
        return false
    }

    private fun checkWinnerForPlayer(player: Player): Boolean {
        for (row in 0..2) {
            for (col in 0..2) {
                if (gameBoard[row][col] == player) {
                    if (checkWinner(row, col)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun isBoardFull(): Boolean {
        for (row in 0..2) {
            for (col in 0..2) {
                if (gameBoard[row][col] == null) return false
            }
        }
        return true
    }

    private fun switchPlayer() {
        currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X
        updateStatus()
    }

    private fun updateStatus() {
        if (gameEnded) return
        
        binding.currentPlayerText.text = when (currentPlayer) {
            Player.X -> getString(R.string.player_x)
            Player.O -> if (gameMode == GameMode.VS_COMPUTER) getString(R.string.computer) else getString(R.string.player_o)
        }
        
        binding.currentPlayerText.setTextColor(getPlayerColor(currentPlayer))
        
        // Update cell interactivity based on computer thinking state
        updateCellInteractivity()
    }
    
    private fun updateCellInteractivity() {
        cells.forEach { cell ->
            cell.isEnabled = !isComputerThinking
            cell.alpha = if (isComputerThinking) 0.5f else 1f
        }
    }

    private fun getPlayerColor(player: Player): Int {
        return when (player) {
            Player.X -> getColor(R.color.x_color)
            Player.O -> getColor(R.color.o_color)
        }
    }

    private fun endGame(winner: Player?) {
        gameEnded = true
        
        val message = when (winner) {
            Player.X -> getString(R.string.winner) + ": " + getString(R.string.player_x)
            Player.O -> getString(R.string.winner) + ": " + (if (gameMode == GameMode.VS_COMPUTER) getString(R.string.computer) else getString(R.string.player_o))
            null -> getString(R.string.draw)
        }
        
        binding.gameStatusText.text = if (winner != null) getString(R.string.winner) else getString(R.string.draw)
        binding.currentPlayerText.text = message
        
        showGameOverDialog(message)
    }

    private fun showGameOverDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.game_over))
            .setMessage(message)
            .setPositiveButton(getString(R.string.play_again)) { _, _ ->
                initializeGame()
            }
            .setNegativeButton(getString(R.string.back_to_menu)) { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun setupClickListeners() {
        binding.newGameButton.setOnClickListener {
            initializeGame()
        }
        
        binding.backToMenuButton.setOnClickListener {
            finish()
        }
    }
}

enum class Player(val symbol: String) {
    X("X"),
    O("O")
}
