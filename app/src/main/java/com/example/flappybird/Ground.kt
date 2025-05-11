package com.example.flappybird

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.content.ContextCompat

class Ground(private val context: Context) {
    
    // Ground properties
    var y: Int = 0
    private val height = 100
    private var x = 0
    private val speed = 5
    
    // Ground texture
    private var groundImage: Bitmap? = null
    
    // Fallback paint
    private val paint = Paint().apply {
        color = Color.rgb(222, 184, 135) // Sand/ground color
    }
    
    init {
        // Position the ground at the bottom of the screen
        y = context.resources.displayMetrics.heightPixels - height
        
        // Load ground image from drawable
        val drawable = ContextCompat.getDrawable(context, R.drawable.ground)
        if (drawable != null) {
            // Create bitmap from vector drawable
            groundImage = Bitmap.createBitmap(500, 100, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(groundImage!!)
            drawable.setBounds(0, 0, 500, 100)
            drawable.draw(canvas)
        }
    }
    
    fun update() {
        // Move the ground to create scrolling effect
        x -= speed
        if (groundImage != null) {
            if (x < -groundImage!!.width) {
                x = 0
            }
        } else {
            if (x < -context.resources.displayMetrics.widthPixels) {
                x = 0
            }
        }
    }
    
    fun draw(canvas: Canvas) {
        if (groundImage != null) {
            // Draw repeating ground texture
            var currentX = x
            while (currentX < canvas.width) {
                canvas.drawBitmap(groundImage!!, currentX.toFloat(), y.toFloat(), null)
                currentX += groundImage!!.width
            }
        } else {
            // Fallback to simple rectangle if image loading failed
            canvas.drawRect(
                0f,
                y.toFloat(),
                canvas.width.toFloat(),
                canvas.height.toFloat(),
                paint
            )
        }
    }
}
