package com.etb.flappybird.game.controller

import android.graphics.Bitmap

class GameConfig() {


    class DiffEasy(){
        var velocity = 2
        var velocityBird = 2
        var velocityCot = 5


        companion object {
            private var instance: DiffEasy? = null

            fun getInstance(): DiffEasy {
                if (instance == null) {
                    instance = DiffEasy()
                }
                return instance!!
            }
        }

    }

    class DiffNormal(){
        var velocity = 3
        var velocityBird = 3
        var velocityCot = 10

        companion object {
            private var instance: DiffNormal? = null

            fun getInstance(): DiffNormal {
                if (instance == null) {
                    instance = DiffNormal()
                }
                return instance!!
            }
        }

    }
    class DiffCut(){
        var velocity = 6
        var velocityBird = 6
        var velocityCot = 15
        companion object {
            private var instance: DiffCut? = null

            fun getInstance(): DiffCut {
                if (instance == null) {
                    instance = DiffCut()
                }
                return instance!!
            }
        }

    }

    class SceneConfig(){
        lateinit var backgroundRun: Bitmap
        lateinit var obstacleTop:Bitmap
        lateinit var obstacleBottom: Bitmap


        // 0 = bottom, 1 = top, 2 = bottom and top
        var obstacleType = 2


           fun setSceneConfig(backgroundRun: Bitmap, obstacleTop: Bitmap, obstacleBottom: Bitmap, obstacleType: Int){
               this.backgroundRun = backgroundRun
               this.obstacleTop = obstacleTop
               this.obstacleBottom = obstacleBottom
               this.obstacleType = obstacleType
            }


        companion object {
            private var instance: SceneConfig? = null

            fun getInstance(): SceneConfig {
                if (instance == null) {
                    instance = SceneConfig()
                }
                return instance!!
            }
        }

    }

    companion object {
        private var instance: GameConfig? = null

        fun getInstance(): GameConfig {
            if (instance == null) {
                instance = GameConfig()
            }
            return instance!!
        }
    }
}