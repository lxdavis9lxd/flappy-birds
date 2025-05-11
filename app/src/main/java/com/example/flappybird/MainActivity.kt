package com.example.flappybird

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var gameView: GameView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Set full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        
        setContentView(R.layout.activity_main)
        
        gameView = findViewById(R.id.gameView)
    }
    
    override fun onPause() {
        super.onPause()
        gameView.pause()
    }
    
    override fun onResume() {
        super.onResume()
        gameView.resume()
    }
}
