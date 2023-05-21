package com.etb.flappybird

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.etb.flappybird.game.activity.SelectActivity
import com.etb.flappybird.game.interfaces.OnItemSelectedListener
import com.etb.flappybird.game.model.ScreenSize
import com.etb.flappybird.game.ui.PlayActivity

class MainActivity : AppCompatActivity(){

    private val Tag = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ScreenSize.getScreenSize(this)
       val btnPlay : Button = findViewById(R.id.m_start)

        btnPlay.setOnClickListener{
            val iPlayGame = Intent(this@MainActivity, SelectActivity::class.java)
            startActivity(iPlayGame)
            finish()
            Log.d(Tag, "Button Play Activated")
        }

    }

}