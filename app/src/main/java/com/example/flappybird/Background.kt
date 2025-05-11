package com.example.flappybird

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.content.ContextCompat

class Background(private val context: Context) {
    
    private val screenWidth = context.resources.displayMetrics.widthPixels
    private val screenHeight = context.resources.displayMetrics.heightPixels
    
    // Background properties
    private var backgroundImage: Bitmap? = null
    private var backgroundX = 0
    private val scrollSpeed = 1
    
    // In case bitmap loading fails
    private val skyPaint = Paint().apply {
        color = Color.rgb(135, 206, 235) // Sky blue
    }
    
    init {
        // Load background image from drawable
        val drawable = ContextCompat.getDrawable(context, R.drawable.background)
        if (drawable != null) {
            backgroundImage = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(backgroundImage!!)
            drawable.setBounds(0, 0, 1000, 1000)
            drawable.draw(canvas)
            
            // Scale background to fit screen height while preserving aspect ratio
            val aspectRatio = backgroundImage!!.width.toFloat() / backgroundImage!!.height.toFloat()
            val targetHeight = screenHeight
            val targetWidth = (targetHeight * aspectRatio).toInt()
            
            backgroundImage = Bitmap.createScaledBitmap(backgroundImage!!, targetWidth, targetHeight, true)
        }
    }
    
    fun update() {
        // Update background position for parallax scrolling
        if (backgroundImage != null) {
            backgroundX -= scrollSpeed
            if (backgroundX < -backgroundImage!!.width) {
                backgroundX = 0
            }
        }
    }
    
    fun draw(canvas: Canvas) {
        if (backgroundImage != null) {
            // Draw scrolling background with seamless tiling
            canvas.drawBitmap(backgroundImage!!, backgroundX.toFloat(), 0f, null)
            canvas.drawBitmap(backgroundImage!!, (backgroundX + backgroundImage!!.width).toFloat(), 0f, null)
        } else {
            // Fallback if image loading failed
            canvas.drawColor(skyPaint.color)
        }
    }
}
