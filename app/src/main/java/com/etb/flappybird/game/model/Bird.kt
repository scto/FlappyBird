
package com.etb.flappybird.game.model

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.etb.flappybird.R
import java.util.ArrayList

class Bird (res : Resources) {


    var x: Int = 0
        get() = field
        set(value) {
            field = value
        }
    var y: Int = 0
        get() = field
        set(value) {
            field = value
        }


    val maxFrame : Int = 8
    var  currentFrame : Int = 0
        get() = field
        set(value) {
            field = value
        }
    lateinit var birbList : ArrayList<Bitmap>
    init {
        birbList = arrayListOf()
        birbList.add(BitmapFactory.decodeResource(res, R.drawable.frame_0))
        birbList.add(BitmapFactory.decodeResource(res, R.drawable.frame_2))
        birbList.add(BitmapFactory.decodeResource(res, R.drawable.frame_3))
        birbList.add(BitmapFactory.decodeResource(res, R.drawable.frame_4))
        birbList.add(BitmapFactory.decodeResource(res, R.drawable.frame_5))
        birbList.add(BitmapFactory.decodeResource(res, R.drawable.frame_6))
        birbList.add(BitmapFactory.decodeResource(res, R.drawable.frame_7))

        x = ScreenSize.SCREEN_WIDTH/2 - birbList[0].width/2
        y = ScreenSize.SCREEN_WIDTH/2 - birbList[0].width/2
    }

    fun getBirb(current: Int): Bitmap {
        if (current >= 0 && current < birbList.size) {
            return birbList[current]
        } else {
            return birbList[0]
        }
    }

}