package com.example.flappybird

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.content.ContextCompat
import kotlin.random.Random

class Pipe(private val context: Context, var x: Int, private val screenHeight: Int) {
    
    companion object {
        const val WIDTH = 80
        const val GAP_HEIGHT = 270
        const val SPEED = 5
    }
    
    // Pipe properties
    private var topHeight: Int = 0
    private var bottomY: Int = 0
    var isPassed = false
    
    // Pipe bitmaps
    private var pipeBitmap: Bitmap? = null
    private val topMatrix = Matrix()
    private val bottomMatrix = Matrix()
    
    // Collision rectangles
    private val topPipeRect = RectF()
    private val bottomPipeRect = RectF()
    
    init {
        // Load pipe image from drawable
        val drawable = ContextCompat.getDrawable(context, R.drawable.pipe)
        if (drawable != null) {
            pipeBitmap = Bitmap.createBitmap(WIDTH, 500, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(pipeBitmap!!)
            drawable.setBounds(0, 0, WIDTH, 500)
            drawable.draw(canvas)
        }
        
        randomizeGap()
    }
    
    fun randomizeGap() {
        // Create a random gap position
        val minTopHeight = 50
        val maxTopHeight = screenHeight - GAP_HEIGHT - 100 // Allow space for bottom pipe
        topHeight = Random.nextInt(minTopHeight, maxTopHeight)
        bottomY = topHeight + GAP_HEIGHT
    }
    
    fun update() {
        // Move pipe to the left
        x -= SPEED
        
        // Update collision rectangles
        topPipeRect.set(x.toFloat(), 0f, (x + WIDTH).toFloat(), topHeight.toFloat())
        bottomPipeRect.set(
            x.toFloat(), 
            bottomY.toFloat(), 
            (x + WIDTH).toFloat(), 
            screenHeight.toFloat()
        )
    }
    
    fun draw(canvas: Canvas) {
        if (pipeBitmap != null) {
            // Draw top pipe (flipped)
            topMatrix.reset()
            topMatrix.postScale(1f, -1f) // Flip vertically
            topMatrix.postTranslate(x.toFloat(), topHeight.toFloat())
            canvas.drawBitmap(pipeBitmap!!, topMatrix, null)
            
            // Draw bottom pipe
            bottomMatrix.reset()
            bottomMatrix.postTranslate(x.toFloat(), bottomY.toFloat())
            canvas.drawBitmap(pipeBitmap!!, bottomMatrix, null)
        } else {
            // Fallback to simple rectangle if bitmap loading failed
            val paint = Paint().apply {
                color = android.graphics.Color.rgb(80, 180, 80) // Green color for pipes
            }
            
            // Draw top pipe
            canvas.drawRect(topPipeRect, paint)
            
            // Draw bottom pipe
            canvas.drawRect(bottomPipeRect, paint)
        }
    }
    
    fun collidesWith(bird: Bird): Boolean {
        // Create bird rectangle for collision detection
        val birdRect = RectF(
            bird.x - bird.width / 2,
            bird.y - bird.height / 2,
            bird.x + bird.width / 2,
            bird.y + bird.height / 2
        )
        
        // Check collision with either pipe
        return RectF.intersects(birdRect, topPipeRect) || 
               RectF.intersects(birdRect, bottomPipeRect)
    }
}
