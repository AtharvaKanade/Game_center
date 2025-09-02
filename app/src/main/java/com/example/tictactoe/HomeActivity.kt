package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tictactoe.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var gamesAdapter: GamesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadGames()
    }

    private fun setupRecyclerView() {
        gamesAdapter = GamesAdapter { game ->
            when (game.id) {
                "tictactoe" -> {
                    // Navigate to Tic Tac Toe game selection
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                "waterjug" -> {
                    // Navigate to Water Jug Puzzle
                    val intent = Intent(this, WaterJugActivity::class.java)
                    startActivity(intent)
                }
                "eightpuzzle" -> {
                    // Navigate to 8-Puzzle
                    val intent = Intent(this, EightPuzzleActivity::class.java)
                    startActivity(intent)
                }
                // Add more games here in the future
            }
        }

        binding.gamesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = gamesAdapter
        }
    }

    private fun loadGames() {
        val games = listOf(
            Game(
                id = "tictactoe",
                title = getString(R.string.app_name),
                description = getString(R.string.tictactoe_description),
                iconResId = R.drawable.ic_tictactoe
            ),
            Game(
                id = "waterjug",
                title = getString(R.string.water_jug_puzzle),
                description = getString(R.string.water_jug_description),
                iconResId = R.drawable.ic_water_jug
            ),
            Game(
                id = "eightpuzzle",
                title = getString(R.string.eight_puzzle),
                description = getString(R.string.eight_puzzle_description),
                iconResId = R.drawable.ic_eight_puzzle
            )
            // Add more games here in the future
        )
        gamesAdapter.submitList(games)
    }
}

data class Game(
    val id: String,
    val title: String,
    val description: String,
    val iconResId: Int
)
