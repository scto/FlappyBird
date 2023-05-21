package com.etb.flappybird.game.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.etb.flappybird.R
import com.etb.flappybird.game.interfaces.OnItemSelectedListener
import com.etb.flappybird.game.model.select.SelectAdapter
import com.etb.flappybird.game.model.select.SelectModel
import com.etb.flappybird.game.ui.PlayActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup


class SelectActivity : AppCompatActivity(), OnItemSelectedListener {

    lateinit var lBase: LinearLayout
    lateinit var  recyclerview: RecyclerView
    lateinit var tvTite: TextView
    var type = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

         lBase = findViewById(R.id.l_base)
         recyclerview = findViewById(R.id.recyclerview)
        tvTite = findViewById(R.id.tv_title)


        initListItem()
        initToggleGroup()
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, PlayActivity::class.java)
            intent.putExtra("type", type)
            startActivity(intent)
        }
    }

    fun initListItem(){

        recyclerview.layoutManager = GridLayoutManager(this, 4)
        val data = ArrayList<SelectModel>()
        data.add(SelectModel("Cenário classico", R.drawable.sc_base))
        data.add(SelectModel("Classic night scenery", R.drawable.sc_base2))
        data.add(SelectModel("Classic night scenery", R.drawable.sc_base2))
        data.add(SelectModel("City 1", R.drawable.sc_base4))
        data.add(SelectModel("City 2", R.drawable.sc_base5))
        val adapter = SelectAdapter(data, this, this)

        recyclerview.adapter = adapter

    }

    fun initToggleGroup() {
        val toggleGroup = findViewById<MaterialButtonToggleGroup>(R.id.toggleGroup)
        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            val checkedButton = group.findViewById<MaterialButton>(checkedId)
            val selectedOption = checkedButton.text.toString()

            when (checkedId) {
                R.id.button1 -> {
                    type = 0
                }
                R.id.button2 -> {
                   type = 1
                }
                R.id.button3 ->{
                    type = 2
                }

            }
        }
    }


    fun initSelectScenario(){
        lBase.visibility = View.VISIBLE
        recyclerview.visibility = View.GONE
    }

    override fun onSelectDifficulty() {
        TODO("Not yet implemented")
    }

    override fun onSelectScenario(tag: String) {
        val TAG = "ON_SCENARIO"
        if (tag.equals(TAG)) initSelectScenario()

    }
}