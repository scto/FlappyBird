package com.etb.flappybird.game.utils

import android.content.Context
import com.etb.flappybird.game.controller.GameController
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogUtils {

    private val gameController = GameController.getInstance()

    fun createDialogDeath(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Voce bateu")
            .setPositiveButton("Reiniciar") { dialog, which ->
                gameController.restartGame()
            }
            .show()


    }

    companion object {
        private var instance: DialogUtils? = null

        fun getInstance(): DialogUtils {
            if (instance == null) {
                instance = DialogUtils()
            }
            return instance!!
        }
    }
}