package com.example.flappybird

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context, attrs: android.util.AttributeSet? = null) :
    SurfaceView(context, attrs), SurfaceHolder.Callback, Runnable {
    
    private var thread: Thread? = null
    private var isPlaying = false
    private var isGameOver = false
    private var isFirstTouch = true
    
    private var screenWidth = 0
    private var screenHeight = 0
    
    private val paint = Paint()
    private val textPaint = Paint()
    
    private var bird: Bird? = null
    private val pipes = ArrayList<Pipe>()
    private var pipeCount = 4
    private var pipeDistance = 300
    
    private var background = Background(context)
    private var ground = Ground(context)
    
    private var score = 0
    private var highScore = 0
    
    private lateinit var prefs: SharedPreferences
    private val PREFS_NAME = "FlappyBirdPrefs"
    private val HIGH_SCORE_KEY = "highScore"
    
    // Sound effects
    private var soundPool: SoundPool? = null
    private var flapSound = 0
    private var pointSound = 0
    private var hitSound = 0
    
    init {
        holder.addCallback(this)
        isFocusable = true
        
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        highScore = prefs.getInt(HIGH_SCORE_KEY, 0)
        
        textPaint.apply {
            color = Color.WHITE
            textSize = 64f
            isFakeBoldText = true
            setShadowLayer(5f, 2f, 2f, Color.BLACK)
        }
        
        // Initialize sounds
        initSounds()
    }

    private fun initSounds() {
        // Initialize sound pool based on API level
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            
            soundPool = SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(attributes)
                .build()
        } else {
            @Suppress("DEPRECATION")
            soundPool = SoundPool(5, AudioManager.STREAM_MUSIC, 0)
        }
        
        // In a real app, load sound resources from the raw directory
        // Since we don't have actual sound files, we'll simulate loading
        try {
            // We would normally load files like this:
            // flapSound = soundPool!!.load(context, R.raw.flap, 1)
            // pointSound = soundPool!!.load(context, R.raw.point, 1)
            // hitSound = soundPool!!.load(context, R.raw.hit, 1)
            
            // For now, we'll load a built-in Android sound
            // if available (beep sound)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                flapSound = soundPool!!.load(context, android.R.raw.keypress_standard, 1)
                pointSound = soundPool!!.load(context, android.R.raw.keypress_standard, 1)
                hitSound = soundPool!!.load(context, android.R.raw.keypress_standard, 1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    override fun surfaceCreated(holder: SurfaceHolder) {
        screenWidth = width
        screenHeight = height
        
        // Initialize game objects
        resetGame()
        
        // Start game loop
        resume()
    }
    
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        screenWidth = width
        screenHeight = height
    }
    
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        pause()
    }
    
    private fun resetGame() {
        isGameOver = false
        isFirstTouch = true
        score = 0
        
        // Initialize bird
        bird = Bird(context, screenWidth / 4f, screenHeight / 2f)
        
        // Initialize pipes
        pipes.clear()
        for (i in 0 until pipeCount) {
            val pipeX = screenWidth + i * (Pipe.WIDTH + pipeDistance)
            pipes.add(Pipe(context, pipeX, screenHeight))
        }
    }
    
    override fun run() {
        while (isPlaying) {
            update()
            draw()
            sleep()
        }
    }
    
    private fun update() {
        if (isGameOver || isFirstTouch) {
            return
        }
        
        background.update()
        ground.update()
        
        bird?.update()
        
        // Update pipes and check collisions
        for (pipe in pipes) {
            pipe.update()
            
            // Check if bird passed the pipe
            if (bird!!.x > pipe.x + Pipe.WIDTH && !pipe.isPassed) {
                pipe.isPassed = true
                score++
                soundPool?.play(pointSound, 1f, 1f, 0, 0, 1f)
            }
            
            // Check collisions with pipes
            if (pipe.collidesWith(bird!!)) {
                gameOver()
                return
            }
            
            // Reposition pipe if it's off screen
            if (pipe.x < -Pipe.WIDTH) {
                pipe.x = screenWidth + (pipeCount - 1) * (Pipe.WIDTH + pipeDistance)
                pipe.randomizeGap()
                pipe.isPassed = false
            }
        }
        
        // Check collision with ground
        if (bird!!.y + bird!!.height > ground.y) {
            gameOver()
        }
        
        // Check if bird went above screen
        if (bird!!.y < 0) {
            bird!!.y = 0f
            bird!!.velocity = 0f
        }
    }
    
    private fun draw() {
        if (holder.surface.isValid) {
            val canvas = holder.lockCanvas()
            
            // Draw background
            background.draw(canvas)
            
            // Draw pipes
            for (pipe in pipes) {
                pipe.draw(canvas)
            }
            
            // Draw ground
            ground.draw(canvas)
            
            // Draw bird
            bird?.draw(canvas)
            
            // Draw score
            canvas.drawText("Score: $score", 20f, 80f, textPaint)
            
            // Draw game over message
            if (isGameOver) {
                val gameOverText = context.getString(R.string.game_over)
                val highScoreText = context.getString(R.string.high_score, highScore)
                val tapToPlayText = context.getString(R.string.tap_to_play)
                
                val textBounds = Rect()
                textPaint.getTextBounds(gameOverText, 0, gameOverText.length, textBounds)
                val textWidth = textBounds.width()
                
                canvas.drawText(gameOverText, (screenWidth - textWidth) / 2f, screenHeight / 2f - 100, textPaint)
                canvas.drawText(highScoreText, (screenWidth - textWidth) / 2f, screenHeight / 2f, textPaint)
                canvas.drawText(tapToPlayText, (screenWidth - textWidth) / 2f, screenHeight / 2f + 100, textPaint)
            } else if (isFirstTouch) {
                val tapToPlayText = context.getString(R.string.tap_to_play)
                val textBounds = Rect()
                textPaint.getTextBounds(tapToPlayText, 0, tapToPlayText.length, textBounds)
                val textWidth = textBounds.width()
                
                canvas.drawText(tapToPlayText, (screenWidth - textWidth) / 2f, screenHeight / 2f, textPaint)
            }
            
            holder.unlockCanvasAndPost(canvas)
        }
    }
    
    private fun gameOver() {
        isGameOver = true
        soundPool?.play(hitSound, 1f, 1f, 0, 0, 1f)
        
        // Update high score if needed
        if (score > highScore) {
            highScore = score
            prefs.edit().putInt(HIGH_SCORE_KEY, highScore).apply()
        }
    }
    
    private fun sleep() {
        try {
            Thread.sleep(17) // ~60 FPS
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
    
    fun pause() {
        isPlaying = false
        try {
            thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
    
    fun resume() {
        isPlaying = true
        thread = Thread(this)
        thread?.start()
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isGameOver) {
                    resetGame()
                } else if (isFirstTouch) {
                    isFirstTouch = false
                } else {
                    bird?.flap()
                    soundPool?.play(flapSound, 0.5f, 0.5f, 0, 0, 1f)
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        soundPool?.release()
        soundPool = null
    }
}
