package com.etb.flappybird.game.model.select.util

import com.etb.flappybird.game.controller.GameController
import com.etb.flappybird.game.interfaces.OnItemSelectedListener

class SelectItemUtils {

    fun addItemOnListener(TAG: String, listener: OnItemSelectedListener) {
        listener.onSelectScenario(TAG)

    }


    companion object {
        private var instance: SelectItemUtils? = null

        fun getInstance(): SelectItemUtils {
            if (instance == null) {
                instance = SelectItemUtils()
            }
            return instance!!
        }
    }

}
