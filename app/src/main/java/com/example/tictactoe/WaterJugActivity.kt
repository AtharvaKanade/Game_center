package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tictactoe.databinding.ActivityWaterJugBinding
import kotlin.random.Random

class WaterJugActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWaterJugBinding
    
    private var jugACapacity = 5
    private var jugBCapacity = 3
    private var jugACurrent = 0
    private var jugBCurrent = 0
    private var targetAmount = 4
    private var moveCount = 0
    private var gameSolved = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityWaterJugBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            setupClickListeners()
            
            // Add a small delay to ensure layout is fully rendered
            binding.root.post {
                try {
                    startNewPuzzle()
                } catch (e: Exception) {
                    android.util.Log.e("WaterJug", "Error in delayed start", e)
                }
            }
        } catch (e: Exception) {
            android.util.Log.e("WaterJug", "Critical error in onCreate", e)
            // Show error dialog to user
            showErrorDialog("Failed to initialize game", e.message ?: "Unknown error")
        }
    }
    
    private fun showErrorDialog(title: String, message: String) {
        try {
            AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Retry") { _, _ ->
                    recreate()
                }
                .setNegativeButton("Exit") { _, _ ->
                    finish()
                }
                .setCancelable(false)
                .show()
        } catch (e: Exception) {
            android.util.Log.e("WaterJug", "Error showing error dialog", e)
            finish()
        }
    }
    
    private fun setupClickListeners() {
        try {
            binding.fillJugAButton.setOnClickListener { fillJugA() }
            binding.emptyJugAButton.setOnClickListener { emptyJugA() }
            binding.fillJugBButton.setOnClickListener { fillJugB() }
            binding.emptyJugBButton.setOnClickListener { emptyJugB() }
            binding.pourAtoBButton.setOnClickListener { pourAtoB() }
            binding.pourBtoAButton.setOnClickListener { pourBtoA() }
            binding.newPuzzleButton.setOnClickListener { startNewPuzzle() }
            binding.backToHomeButton.setOnClickListener { finish() }
            
            android.util.Log.d("WaterJug", "Click listeners setup completed")
        } catch (e: Exception) {
            android.util.Log.e("WaterJug", "Error setting up click listeners", e)
        }
    }
    
    private fun startNewPuzzle() {
        try {
            // Generate random puzzle with different jug capacities
            val puzzles = listOf(
                Triple(5, 3, 4),   // Classic 5L, 3L -> 4L
                Triple(7, 4, 5),   // 7L, 4L -> 5L
                Triple(8, 5, 6),   // 8L, 5L -> 6L
                Triple(9, 6, 7),   // 9L, 6L -> 7L
                Triple(10, 7, 8),  // 10L, 7L -> 8L
                Triple(6, 4, 5),   // 6L, 4L -> 5L
                Triple(9, 5, 7),   // 9L, 5L -> 7L
                Triple(11, 6, 9),  // 11L, 6L -> 9L
                Triple(13, 7, 10), // 13L, 7L -> 10L
                Triple(15, 8, 12), // 15L, 8L -> 12L
                Triple(12, 8, 10), // 12L, 8L -> 10L
                Triple(14, 9, 11), // 14L, 9L -> 11L
                Triple(16, 10, 13), // 16L, 10L -> 13L
                Triple(18, 11, 15), // 18L, 11L -> 15L
                Triple(20, 12, 17)  // 20L, 12L -> 17L
            )
            
            // Filter to only solvable puzzles
            val solvablePuzzles = puzzles.filter { (a, b, target) ->
                isPuzzleSolvable(a, b, target)
            }
            
            // Ensure we have at least one solvable puzzle
            if (solvablePuzzles.isEmpty()) {
                // Fallback to classic solvable puzzle
                jugACapacity = 5
                jugBCapacity = 3
                targetAmount = 4
            } else {
                val puzzle = solvablePuzzles.random()
                jugACapacity = puzzle.first
                jugBCapacity = puzzle.second
                targetAmount = puzzle.third
            }
            
            jugACurrent = 0
            jugBCurrent = 0
            moveCount = 0
            gameSolved = false
            
            android.util.Log.d("WaterJug", "New puzzle: ${jugACapacity}L + ${jugBCapacity}L -> ${targetAmount}L (Solvable: ${isPuzzleSolvable(jugACapacity, jugBCapacity, targetAmount)})")
            
            updateDisplay()
            updateWaterLevels()
            checkWinCondition()
        } catch (e: Exception) {
            android.util.Log.e("WaterJug", "Error starting new puzzle", e)
        }
    }
    
    private fun isPuzzleSolvable(a: Int, b: Int, target: Int): Boolean {
        try {
            // A puzzle is solvable if target can be expressed as:
            // target = x*a + y*b where x and y are integers (can be negative)
            // This is equivalent to: target must be divisible by GCD(a,b)
            
            val gcd = findGCD(a, b)
            return target % gcd == 0 && target <= maxOf(a, b)
        } catch (e: Exception) {
            android.util.Log.e("WaterJug", "Error checking puzzle solvability", e)
            return false
        }
    }
    
    private fun findGCD(a: Int, b: Int): Int {
        return if (b == 0) a else findGCD(b, a % b)
    }
    
    private fun fillJugA() {
        try {
            if (gameSolved) return
            jugACurrent = jugACapacity
            moveCount++
            updateDisplay()
            updateWaterLevels()
            checkWinCondition()
            animateButton(binding.fillJugAButton)
        } catch (e: Exception) {
            android.util.Log.e("WaterJug", "Error filling jug A", e)
        }
    }
    
    private fun emptyJugA() {
        try {
            if (gameSolved) return
            jugACurrent = 0
            moveCount++
            updateDisplay()
            updateWaterLevels()
            checkWinCondition()
            animateButton(binding.emptyJugAButton)
        } catch (e: Exception) {
            android.util.Log.e("WaterJug", "Error emptying jug A", e)
        }
    }
    
    private fun fillJugB() {
        try {
            if (gameSolved) return
            jugBCurrent = jugBCapacity
            moveCount++
            updateDisplay()
            updateWaterLevels()
            checkWinCondition()
            animateButton(binding.fillJugBButton)
        } catch (e: Exception) {
            android.util.Log.e("WaterJug", "Error filling jug B", e)
        }
    }
    
    private fun emptyJugB() {
        try {
            if (gameSolved) return
            jugBCurrent = 0
            moveCount++
            updateDisplay()
            updateWaterLevels()
            checkWinCondition()
            animateButton(binding.emptyJugBButton)
        } catch (e: Exception) {
            android.util.Log.e("WaterJug", "Error emptying jug B", e)
        }
    }
    
    private fun pourAtoB() {
        try {
            if (gameSolved) return
            
            val spaceInB = jugBCapacity - jugBCurrent
            val amountToPour = minOf(jugACurrent, spaceInB)
            
            if (amountToPour > 0) {
                jugACurrent -= amountToPour
                jugBCurrent += amountToPour
                moveCount++
                updateDisplay()
                updateWaterLevels()
                checkWinCondition()
                animateButton(binding.pourAtoBButton)
            }
        } catch (e: Exception) {
            android.util.Log.e("WaterJug", "Error pouring A to B", e)
        }
    }
    
    private fun pourBtoA() {
        try {
            if (gameSolved) return
            
            val spaceInA = jugACapacity - jugACurrent
            val amountToPour = minOf(jugBCurrent, spaceInA)
            
            if (amountToPour > 0) {
                jugBCurrent -= amountToPour
                jugACurrent += amountToPour
                moveCount++
                updateDisplay()
                updateWaterLevels()
                checkWinCondition()
                animateButton(binding.pourBtoAButton)
            }
        } catch (e: Exception) {
            android.util.Log.e("WaterJug", "Error pouring B to A", e)
        }
    }
    
    private fun updateDisplay() {
        try {
            binding.jugACapacity.text = "${jugACapacity}L"
            binding.jugBCapacity.text = "${jugBCapacity}L"
            binding.jugACurrent.text = "${jugACurrent}L"
            binding.jugBCurrent.text = "${jugBCurrent}L"
            binding.targetText.text = getString(R.string.target_amount, targetAmount)
            binding.movesText.text = getString(R.string.moves, moveCount)
        } catch (e: Exception) {
            android.util.Log.e("WaterJug", "Error updating display", e)
        }
    }
    
    private fun updateWaterLevels() {
        try {
            // Validate that views exist and are accessible
            if (!::binding.isInitialized || binding.jugAWater == null || binding.jugBWater == null) {
                android.util.Log.w("WaterJug", "Views not accessible, skipping water level update")
                return
            }
            
            // Calculate water ratios
            val jugARatio = if (jugACapacity > 0) {
                (jugACurrent.toFloat() / jugACapacity).coerceIn(0f, 1f)
            } else {
                0f
            }
            
            val jugBRatio = if (jugBCapacity > 0) {
                (jugBCurrent.toFloat() / jugBCapacity).coerceIn(0f, 1f)
            } else {
                0f
            }
            
            // Update water level heights with smooth animation
            try {
                // Calculate target heights (water area is approximately 120dp)
                val maxWaterHeight = 120
                val jugAHeight = (jugARatio * maxWaterHeight).toInt()
                val jugBHeight = (jugBRatio * maxWaterHeight).toInt()
                
                // Animate water level changes
                animateWaterLevel(binding.jugAWater, jugAHeight, 500)
                animateWaterLevel(binding.jugBWater, jugBHeight, 500)
                
                android.util.Log.d("WaterJug", "Water heights - A: ${jugAHeight}dp (${jugARatio}), B: ${jugBHeight}dp (${jugBRatio})")
            } catch (e: Exception) {
                android.util.Log.w("WaterJug", "Could not update water heights, continuing with other updates", e)
            }
            
            // Set water colors based on jug type and fullness
            try {
                // Create dynamic color state lists based on water level
                val jugAColor = createWaterColor(R.color.primary, jugARatio)
                val jugBColor = createWaterColor(R.color.secondary, jugBRatio)
                
                binding.jugAWater.setBackgroundTintList(jugAColor)
                binding.jugBWater.setBackgroundTintList(jugBColor)
                
                android.util.Log.d("WaterJug", "Water colors updated with intensity based on fullness")
            } catch (e: Exception) {
                android.util.Log.w("WaterJug", "Could not update background tint, continuing", e)
            }
            
            android.util.Log.d("WaterJug", "Water levels updated - A: ${jugACurrent}L/${jugACapacity}L (${jugARatio}), B: ${jugBCurrent}L/${jugBCapacity}L (${jugBRatio})")
        } catch (e: Exception) {
            android.util.Log.e("WaterJug", "Error updating water levels", e)
        }
    }
    
    private fun animateWaterLevel(waterView: View, targetHeight: Int, duration: Long) {
        try {
            // Get current layout params
            val layoutParams = waterView.layoutParams
            if (layoutParams == null) {
                android.util.Log.w("WaterJug", "Layout params null, cannot animate water level")
                return
            }
            
            val currentHeight = layoutParams.height
            val animator = android.animation.ValueAnimator.ofInt(currentHeight, targetHeight)
            
            animator.duration = duration
            animator.interpolator = android.view.animation.DecelerateInterpolator()
            
            animator.addUpdateListener { animation ->
                try {
                    val animatedHeight = animation.animatedValue as Int
                    layoutParams.height = animatedHeight
                    waterView.requestLayout()
                } catch (e: Exception) {
                    android.util.Log.w("WaterJug", "Error updating animated height", e)
                }
            }
            
            // Add completion listener for visual feedback
            animator.addListener(object : android.animation.Animator.AnimatorListener {
                override fun onAnimationStart(animation: android.animation.Animator) {
                    // Add a subtle scale effect when animation starts
                    waterView.animate()
                        .scaleX(1.05f)
                        .scaleY(1.05f)
                        .setDuration(200)
                        .start()
                }
                
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    // Return to normal scale when animation ends
                    waterView.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(200)
                        .start()
                    
                    // Add a subtle wave effect after water settles
                    addWaterWaveEffect(waterView)
                }
                
                override fun onAnimationCancel(animation: android.animation.Animator) {}
                override fun onAnimationRepeat(animation: android.animation.Animator) {}
            })
            
            animator.start()
            
        } catch (e: Exception) {
            android.util.Log.e("WaterJug", "Error animating water level", e)
        }
    }
    
    private fun createWaterColor(baseColorRes: Int, fullnessRatio: Float): android.content.res.ColorStateList {
        try {
            val baseColor = ContextCompat.getColor(this, baseColorRes)
            
            // Adjust color intensity based on fullness
            // More water = more intense color, less water = more transparent
            val alpha = (0.3f + fullnessRatio * 0.7f).coerceIn(0.3f, 1.0f)
            
            // Create a new color with adjusted alpha
            val adjustedColor = android.graphics.Color.argb(
                (alpha * 255).toInt(),
                android.graphics.Color.red(baseColor),
                android.graphics.Color.green(baseColor),
                android.graphics.Color.blue(baseColor)
            )
            
            return android.content.res.ColorStateList.valueOf(adjustedColor)
        } catch (e: Exception) {
            android.util.Log.w("WaterJug", "Error creating water color, using default", e)
            return ContextCompat.getColorStateList(this, baseColorRes) ?: android.content.res.ColorStateList.valueOf(android.graphics.Color.BLUE)
        }
    }
    
    private fun addWaterWaveEffect(waterView: View) {
        try {
            // Create a subtle wave animation by slightly adjusting the scale
            waterView.animate()
                .scaleX(1.02f)
                .scaleY(1.02f)
                .setDuration(300)
                .withEndAction {
                    waterView.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .start()
                }
                .start()
        } catch (e: Exception) {
            android.util.Log.w("WaterJug", "Error adding water wave effect", e)
        }
    }
    
    private fun checkWinCondition() {
        try {
            if (jugACurrent == targetAmount || jugBCurrent == targetAmount) {
                gameSolved = true
                showWinDialog()
            }
        } catch (e: Exception) {
            android.util.Log.e("WaterJug", "Error checking win condition", e)
        }
    }
    
    private fun showWinDialog() {
        try {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.puzzle_solved))
                .setMessage(getString(R.string.you_solved, moveCount))
                .setPositiveButton(getString(R.string.try_another)) { _, _ ->
                    startNewPuzzle()
                }
                .setNegativeButton(getString(R.string.back_to_home)) { _, _ ->
                    finish()
                }
                .setCancelable(false)
                .show()
        } catch (e: Exception) {
            android.util.Log.e("WaterJug", "Error showing win dialog", e)
        }
    }
    
    private fun animateButton(view: View) {
        try {
            // Simple button animation without complex effects
            view.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction {
                    view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
                .start()
        } catch (e: Exception) {
            android.util.Log.e("WaterJug", "Error animating button", e)
        }
    }
}
