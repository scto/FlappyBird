package com.etb.flappybird.game.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.etb.flappybird.game.thread.PlayThread

class PlayView(context: Context?, type: Int) : SurfaceView(context), SurfaceHolder.Callback {

    private val TAG = "PlayView"
    private var playThread: PlayThread? = null
    var mType = 1

    var mContext = context

    init {
        val holder = holder
        holder.addCallback(this)
        mType = type
        isFocusable = true
        playThread = mContext?.let { PlayThread(holder, resources, it, mType) }

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.i("OnTOUCH", "onTouchEvent called")

        event?.let {
            val x = it.x.toInt()
            val y = it.y.toInt()

            Log.i("X AND Y", "X: $x \nY: $y")

            val ev = it.action
            if (ev == MotionEvent.ACTION_DOWN) {
                playThread?.jump()
                if (x >=1011 && y <= 100) {
                    // Ação de clique na imagem de pausa
                    playThread?.onClickPause()
                }
            }
        }

        return true
    }



    override fun surfaceCreated(holder: SurfaceHolder) {
        if (playThread == null) { // Verifique se playThread é nulo
            playThread = mContext?.let { PlayThread(holder, resources, it, mType) }
        } else {
            playThread!!.start()
        }
    }


    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

     }

     override fun surfaceDestroyed(holder: SurfaceHolder) {
         if(playThread!!.isRunning){
             playThread!!.isRunning = false
             var isCheck : Boolean = true
             while (isCheck) {
                 try{
                     playThread!!.join()
                     isCheck = false
                 }catch (e : InterruptedException) {
                     e.printStackTrace()

                 }
             }
         }
     }

    fun restartGame() {
        playThread?.run { resetGame() }
        playThread?.interrupt()
        playThread = mContext?.let { PlayThread(holder, resources, it, mType) }
        playThread?.start()
    }


 }