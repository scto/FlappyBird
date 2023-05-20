package com.etb.flappybird.game.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.etb.flappybird.game.thread.PlayThread

class PlayView(context: Context?) : SurfaceView(context), SurfaceHolder.Callback {

    private val TAG = "PlayView"
    private var playThread : PlayThread? = null

    var mContext = context

    init {
        val holder = holder
        holder.addCallback(this)
        isFocusable = true
        playThread = mContext?.let { PlayThread(holder, resources, it) }

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.i("OnTOUCH", "onTouchEvent called")
        val ev = event?.action

        if (ev == MotionEvent.ACTION_DOWN) {
            playThread?.jump()
        }
        playThread?.jump()
        return true
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        if (playThread == null) { // Verifique se playThread é nulo
            playThread = mContext?.let { PlayThread(holder, resources, it) }
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
        playThread?.interrupt()
        playThread = mContext?.let { PlayThread(holder, resources, it) }
        playThread?.start()
    }


 }