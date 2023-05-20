package com.etb.flappybird.game.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.etb.flappybird.game.thread.PlayThread
import com.etb.flappybird.game.ui.view.PlayView

class PlayActivity : AppCompatActivity(){

    private var playThread : PlayThread? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val playView = PlayView(this)
        setContentView(playView)

    /*    playView.setOnTouchListener { _, event ->
            Log.i("OnTOUCH", "onTouchEvent called")
            if (event.action == MotionEvent.ACTION_DOWN) {

            }
            true
        }*/
    }

}
