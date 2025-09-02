package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.vsComputerButton.setOnClickListener {
            startGame(GameMode.VS_COMPUTER)
        }

        binding.vsFriendButton.setOnClickListener {
            startGame(GameMode.VS_FRIEND)
        }

        binding.backToHomeButton.setOnClickListener {
            finish()
        }
    }

    private fun startGame(gameMode: GameMode) {
        try {
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra("GAME_MODE", gameMode.name)
            }
            startActivity(intent)
        } catch (e: Exception) {
            // Log the error and show a user-friendly message
            android.util.Log.e("MainActivity", "Error starting game: ${e.message}", e)
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Failed to start game: ${e.message}")
                .setPositiveButton("OK", null)
                .show()
        }
    }
}

enum class GameMode {
    VS_COMPUTER,
    VS_FRIEND
}
