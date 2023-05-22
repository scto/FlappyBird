package com.etb.flappybird.game.model.select

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.etb.flappybird.R
import com.etb.flappybird.game.activity.SelectActivity
import com.etb.flappybird.game.controller.GameConfig
import com.etb.flappybird.game.controller.GameController
import com.etb.flappybird.game.interfaces.ItemSelected
import com.etb.flappybird.game.interfaces.OnItemSelectedListener
import com.etb.flappybird.game.model.select.util.SelectItemUtils
import com.etb.flappybird.game.ui.PlayActivity

class SelectAdapter(private val mList: List<SelectModel>, private val context: Context, private val listener: OnItemSelectedListener) : RecyclerView.Adapter<SelectAdapter.ViewHolder>() {

   val TAG = "ON_SCENARIO"
     val selectItemUtils = SelectItemUtils.getInstance()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_select, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectAdapter.ViewHolder, position: Int) {
        val itemsViewModel = mList[position]

        val backgroundRun: Bitmap = itemsViewModel.backgroundRun
        val obstacleTop: Bitmap = itemsViewModel.obstacleTop
        val obstacleBottom: Bitmap = itemsViewModel.obstacleBottom
        val obstacleType: Int = itemsViewModel.obstacleType
        val sceneConfig = GameConfig.SceneConfig.getInstance()



        holder.itemImage.setImageResource(itemsViewModel.image)
        holder.itemTitle.text = itemsViewModel.title
        holder.itemBase.setOnClickListener{

            val obj = ItemSelected()
            sceneConfig.setSceneConfig( backgroundRun, obstacleTop, obstacleBottom, obstacleType)
            selectItemUtils.addItemOnListener(TAG, listener)


           /* val intent = Intent(holder.itemView.context, PlayActivity::class.java)
            holder.itemView.context.startActivity(intent)*/
        }
    }



    override fun getItemCount(): Int {
       return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val itemBase: LinearLayout = itemView.findViewById(R.id.ly_item)
        val itemImage: ImageView = itemView.findViewById(R.id.iv_image)
        val itemTitle: TextView = itemView.findViewById(R.id.tv_title)
    }
}