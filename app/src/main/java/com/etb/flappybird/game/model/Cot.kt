package com.etb.flappybird.game.model

import android.content.res.Resources
import android.graphics.BitmapFactory
import com.etb.flappybird.R
import com.etb.flappybird.game.controller.GameConfig

class Cot(res: Resources) {
    val sceneConfig = GameConfig.SceneConfig.getInstance()
    val cotTop =sceneConfig.obstacleTop
        get() = field
    val cotBottom = sceneConfig.obstacleBottom
        get() = field

    val w = cotTop.width
    val h = cotTop.height

    var x : Int = 0
        get() = field
        set(value) {
            field = value
        }
    var ccY : Int = 0
        get() = field
        set(value) {
            field = value
        }
    fun getTopY() : Int{
        return ccY - h
    }

   fun getBottomY() : Int {
        return ccY + 500
    }


}