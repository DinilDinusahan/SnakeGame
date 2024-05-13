package com.example.snake_game

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ScoreActivity : AppCompatActivity() {
    private lateinit var yourScoreTextView: TextView
    private lateinit var highScoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        // Initialize TextViews
        yourScoreTextView = findViewById(R.id.tv_your_score)
        highScoreTextView = findViewById(R.id.tv_high_score)

        // Get the scores from the intent
        val yourScore = intent.getIntExtra("yourScore", 0)
        val highScore = intent.getIntExtra("highScore", 0)

        // Display the scores
        yourScoreTextView.text = "Your Score: $yourScore"
        highScoreTextView.text = "High Score: $highScore"

        //Set up the reset button click listener
        val resetButton = findViewById<Button>(R.id.reset_high_score_button)
        resetButton.setOnClickListener {
            resetHighScore()
        }
        val back: Button = findViewById(R.id.back)
        back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun resetHighScore() {
        // Reset high score to 0 (or any initial value you prefer)
        val sharedPreferences = getSharedPreferences("SnakeGamePreferences", MODE_PRIVATE)
        sharedPreferences.edit().putInt("highScore", 0).apply()


        highScoreTextView.text = "High Score: 0"

        // Show a toast message high score reset
        Toast.makeText(this, "High Score Reset to 0", Toast.LENGTH_SHORT).show()

        // Inside ScoreActivity
        val highScore = sharedPreferences.getInt("highScore", 0)
        highScoreTextView.text = "High Score: $highScore"

    }
}
