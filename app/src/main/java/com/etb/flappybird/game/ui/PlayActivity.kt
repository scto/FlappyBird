package com.etb.flappybird.game.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.etb.flappybird.game.controller.GameController
import com.etb.flappybird.game.thread.PlayThread
import com.etb.flappybird.game.ui.view.PlayView

class PlayActivity : AppCompatActivity(){



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val playView = PlayView(this)
        val gameController = GameController.getInstance()
        gameController.setPlayView(playView)
        setContentView(playView)


    }

}
