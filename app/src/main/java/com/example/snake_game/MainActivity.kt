package com.example.snake_game


import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt
import android.util.Log


class MainActivity : Activity() {

    private lateinit var highScoreTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("SnakeGamePreferences", MODE_PRIVATE)

        //  high score TextView
        highScoreTextView = findViewById(R.id.high_score)

        // Retrieve and display high score
        val highScore = sharedPreferences.getInt("highScore", 0)
        highScoreTextView.text = "High Score: $highScore"

        val board = findViewById<RelativeLayout>(R.id.board)
        val border = findViewById<RelativeLayout>(R.id.relativeLayout)
        val lilu = findViewById<LinearLayout>(R.id.lilu)
        val upButton = findViewById<Button>(R.id.up)
        val downButton = findViewById<Button>(R.id.down)
        val leftButton = findViewById<Button>(R.id.left)
        val rightButton = findViewById<Button>(R.id.right)
        val pauseButton = findViewById<Button>(R.id.pause)
        val newgame = findViewById<Button>(R.id.new_game)
        val resume = findViewById<Button>(R.id.resume)
        val playagain = findViewById<Button>(R.id.playagain)
        val score = findViewById<Button>(R.id.score)
        val score2 = findViewById<Button>(R.id.score2)
        val coffee = ImageView(this)
        val snake = ImageView(this)
        val snakeSegments = mutableListOf(snake) // Keep track of the position of each snake segment
        val handler = Handler()
        var delayMillis = 30L // Update snake position every 100 milliseconds
        var currentDirection = "right" // Start moving right by default
        var scorex = 0
        var highScoreUpdated = false


        // Hide unnecessary UI elements initially
        board.visibility = View.INVISIBLE
        playagain.visibility = View.INVISIBLE
        score.visibility = View.INVISIBLE
        score2.visibility = View.INVISIBLE


        newgame.setOnClickListener {
            // Show game board and hide buttons
            board.visibility = View.VISIBLE
            newgame.visibility = View.INVISIBLE
            resume.visibility = View.INVISIBLE
            score2.visibility = View.VISIBLE

            //snake
            snake.setImageResource(R.drawable.snake)
            snake.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            board.addView(snake)
            snakeSegments.add(snake)


            var snakeX = snake.x
            var snakeY = snake.y

            //coffee
            coffee.setImageResource(R.drawable.coffee)
            coffee.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            board.addView(coffee)

            // Generate random position for coffee
            val random = Random()
            val randomX =
                random.nextInt(801) - 400
            val randomY =
                random.nextInt(801) - 400


            coffee.x = randomX.toFloat()
            coffee.y = randomY.toFloat()




            fun checkFoodCollision() {
                val distanceThreshold = 50
                val distance = sqrt((snake.x - coffee.x).pow(2) + (snake.y - coffee.y).pow(2))

                if (distance < distanceThreshold) { // Check if the distance between the snake head and the coffee is less than the threshold

                    // Add new segment to snake
                    val newSnake = ImageView(this)
                    newSnake.setImageResource(R.drawable.snake)
                    newSnake.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    board.addView(newSnake)

                    snakeSegments.add(newSnake) // Add the new snake segment

                    // Generate new position for coffee
                    val randomX = random.nextInt(801) - -100
                    val randomY = random.nextInt(801) - -100


                    coffee.x = randomX.toFloat()
                    coffee.y = randomY.toFloat()

                    delayMillis--
                    scorex++

                    score2.text =   "score : " + scorex.toString()
                }
            }



            // Runnable to update snake position
            val runnable = object : Runnable {
                override fun run() {

                    for (i in snakeSegments.size - 1 downTo 1) {
                        snakeSegments[i].x = snakeSegments[i - 1].x
                        snakeSegments[i].y = snakeSegments[i - 1].y
                    }


                    // Handle collision with border
                    when (currentDirection) {
                        "up" -> {
                            snakeY -= 10
                            if (snakeY < -490) {
                                snakeY = -490f
                                border.setBackgroundColor(getResources().getColor(R.color.red))
                                playagain.visibility = View.VISIBLE
                                currentDirection = "pause"
                                lilu.visibility = View.INVISIBLE

                                score2.visibility = View.INVISIBLE
                            }
                            snake.translationY = snakeY
                        }
                        "down" -> {
                            snakeY += 10
                            val maxY =
                                board.height / 2 - snake.height + 30 // Calculate the maximum y coordinate
                            if (snakeY > maxY) {
                                snakeY = maxY.toFloat()
                                border.setBackgroundColor(getResources().getColor(R.color.red))
                                playagain.visibility = View.VISIBLE
                                currentDirection = "pause"
                                lilu.visibility = View.INVISIBLE

                                score2.visibility = View.INVISIBLE

                            }
                            snake.translationY = snakeY
                        }
                        "left" -> {
                            snakeX -= 10
                            if (snakeX < -490) {
                                snakeX = -490f
                                border.setBackgroundColor(getResources().getColor(R.color.red))
                                playagain.visibility = View.VISIBLE
                                currentDirection = "pause"
                                lilu.visibility = View.INVISIBLE
                                score2.visibility = View.INVISIBLE
                            }
                            snake.translationX = snakeX
                        }
                        "right" -> {
                            snakeX += 10
                            val maxX =
                                board.height / 2 - snake.height + 30
                            if (snakeX > maxX) {
                                snakeX = maxX.toFloat()
                                border.setBackgroundColor(getResources().getColor(R.color.red))
                                playagain.visibility = View.VISIBLE
                                currentDirection = "pause"
                                lilu.visibility = View.INVISIBLE
                                score2.visibility = View.INVISIBLE
                            }
                            snake.translationX = snakeX
                        }

                        "pause" -> {
                            snakeX += 0
                            snake.translationX = snakeX
                        }
                    }
                    // Start the game loop
                    checkFoodCollision()
                    handler.postDelayed(this, delayMillis)
                }
            }

            handler.postDelayed(runnable, delayMillis)


            upButton.setOnClickListener {
                currentDirection = "up"
            }
            downButton.setOnClickListener {
                currentDirection = "down"
            }
            leftButton.setOnClickListener {
                currentDirection = "left"
            }
            rightButton.setOnClickListener {
                currentDirection = "right"
            }
            pauseButton.setOnClickListener {
                currentDirection = "pause"
                board.visibility = View.INVISIBLE
                newgame.visibility = View.VISIBLE
                resume.visibility = View.VISIBLE

            }
            resume.setOnClickListener {
                currentDirection = "right"
                board.visibility = View.VISIBLE
                newgame.visibility = View.INVISIBLE
                resume.visibility = View.INVISIBLE

            }
            val playagain = findViewById<Button>(R.id.playagain)

            playagain.setOnClickListener {
                // Update high score if the current score is higher
                if (scorex > highScore && !highScoreUpdated) {
                    val editor = sharedPreferences.edit()
                    editor.putInt("highScore", scorex)
                    editor.apply()
                    highScoreTextView.text = "High Score: $scorex"
                    Log.d("HighScore", "Updated High Score: $scorex")
                    highScoreUpdated = true
                }

                // Retrieve the updated high score from SharedPreferences
                val updatedHighScore = sharedPreferences.getInt("highScore", 0)

                // Start ScoreActivity with the updated high score
                val intent = Intent(this, ScoreActivity::class.java)
                intent.putExtra("yourScore", scorex) // Pass current score
                intent.putExtra("highScore", updatedHighScore) // Pass high score
                startActivity(intent)
                finish()
            }


        }


    }

}