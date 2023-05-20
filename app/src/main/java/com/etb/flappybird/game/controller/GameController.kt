package com.etb.flappybird.game.controller

import com.etb.flappybird.game.ui.view.PlayView

class GameController {

    private lateinit var playView: PlayView

    fun setPlayView(playView: PlayView) {
        this.playView = playView
    }

    fun restartGame() {
        playView.restartGame()
        // Outras operações de reinicialização, se necessário
    }

    companion object {
        private var instance: GameController? = null

        fun getInstance(): GameController {
            if (instance == null) {
                instance = GameController()
            }
            return instance!!
        }
    }
}