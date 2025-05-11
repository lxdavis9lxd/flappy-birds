package com.example.flappybird

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat

class Bird(private val context: Context, var x: Float, var y: Float) {
    
    // Bird properties
    var width: Int = 0
    var height: Int = 0
    var velocity: Float = 0f
    private val gravity: Float = 0.6f
    private val flapPower: Float = -10f
    
    // Animation properties
    private val birdFrames = arrayOfNulls<Bitmap>(3)
    private var frameIndex = 0
    private var frameCounter = 0
    private val frameDelay = 5
    
    // Rotation
    private val matrix = Matrix()
    private var rotation = 0f
    
    init {
        // Set bird dimensions
        width = 70
        height = 50
        
        // Load bird animation frames from vector drawables
        val drawableIds = arrayOf(
            R.drawable.bird1, 
            R.drawable.bird2, 
            R.drawable.bird3
        )
        
        for (i in drawableIds.indices) {
            val drawable = ContextCompat.getDrawable(context, drawableIds[i])
            if (drawable != null) {
                // Convert drawable to bitmap
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, width, height)
                drawable.draw(canvas)
                birdFrames[i] = bitmap
            } else {
                // Fallback in case drawable loading fails
                birdFrames[i] = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(birdFrames[i]!!)
                val paint = Paint().apply { 
                    color = when (i) {
                        0 -> android.graphics.Color.YELLOW
                        1 -> android.graphics.Color.rgb(255, 200, 0)
                        else -> android.graphics.Color.rgb(255, 180, 0)
                    }
                }
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            }
        }
    }
    
    fun update() {
        // Apply gravity
        velocity += gravity
        y += velocity
        
        // Update animation
        frameCounter++
        if (frameCounter > frameDelay) {
            frameCounter = 0
            frameIndex = (frameIndex + 1) % 3
        }
        
        // Update rotation based on velocity
        rotation = if (velocity < 0) {
            -25f // Point up when flapping
        } else {
            // Gradually rotate down as falling
            Math.min(90f, rotation + 2f)
        }
    }
    
    fun draw(canvas: Canvas) {
        // Save canvas state
        canvas.save()
        
        // Apply rotation around bird center
        matrix.reset()
        matrix.postRotate(rotation, width / 2f, height / 2f)
        matrix.postTranslate(x - width / 2f, y - height / 2f)
        
        // Draw the current frame
        canvas.drawBitmap(birdFrames[frameIndex]!!, matrix, null)
        
        // Restore canvas state
        canvas.restore()
    }
    
    fun flap() {
        velocity = flapPower
    }
}
