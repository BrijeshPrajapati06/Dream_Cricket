package com.diyantech.dreamcricket.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diyantech.dreamcricket.R
import com.diyantech.dreamcricket.lib.BitmapManager
import com.diyantech.dreamcricket.model.ModelPlayer
import de.hdodenhof.circleimageview.CircleImageView

class PlayerAdapter() : RecyclerView.Adapter<PlayerAdapter.ViewHolder>() {

  private var playerList = mutableListOf<ModelPlayer>()
    private var action_edit : ((ModelPlayer) -> Unit)? = null
    lateinit var context : Context
    var modelPlayer : ModelPlayer? = null
    var bitmapManager : BitmapManager? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_add_player, parent, false)
        return PlayerAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val team = playerList[position]

        holder.playerName?.text = team.playername
        holder.team_name?.text = team.teamname
        holder.txt_multi_name?.text = team.multiName

        holder.txt_player_edit?.setOnClickListener {
            action_edit?.invoke(team)
        }

        if (modelPlayer?.playerImg!=null){
            Glide.with(context)
                .load(modelPlayer?.playerImg)
                .into(holder.iv_team_profile!!)
        }

    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    fun setPlayerData(data : List<ModelPlayer>){
        playerList.apply {
            clear()
            addAll(data)
        }
    }

    fun setOnActionEditPlayerListner(callback:(ModelPlayer)->Unit){
        this.action_edit = callback
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var playerName: TextView? = itemView.findViewById(R.id.playerName)
        var team_name: TextView? = itemView.findViewById(R.id.team_name)
        var txt_multi_name: TextView? = itemView.findViewById(R.id.txt_multi_name)
        var txt_player_edit: ImageView? = itemView.findViewById(R.id.txt_player_edit)
        var iv_team_profile: CircleImageView? = itemView.findViewById(R.id.iv_team_profile)

    }
}