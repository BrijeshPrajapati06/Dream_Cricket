package com.diyantech.dreamcricket.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.diyantech.dreamcricket.R
import com.diyantech.dreamcricket.model.ModelMatch
import com.diyantech.dreamcricket.model.ModelTeam
import de.hdodenhof.circleimageview.CircleImageView

class TeamAdapter : RecyclerView.Adapter<TeamAdapter.ViewHolder>() {
    private var teamList = mutableListOf<ModelTeam>()
    private var action_edit_team : ((ModelTeam) -> Unit)? = null
    var context: Context? = null
    var modelTeam : ModelTeam? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.iem_add_team, parent, false)
        return TeamAdapter.ViewHolder(view)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val team = teamList[position]

        holder.teamName?.text = team.team
        holder.txt_total_player?.text = team.total
//        holder.iv_team_profile.load(modelTeam?.profilePhoto)

        if (modelTeam?.teamPhoto!=null){
            Glide.with(context!!)
                .load(modelTeam?.teamPhoto)
                .into(holder.iv_team_profile!!)
        }
//
        holder.iv_team_profile?.let {
            context?.let { it1 ->
                Glide.with(it1)
                    .load(team.data)
                    .into(it)
            }
        }

        holder.img_edit?.setOnClickListener {
            action_edit_team?.invoke(team)
        }

    }


    override fun getItemCount(): Int {
        return teamList.size

    }

    fun setTeamData(data : List<ModelTeam>){
        teamList.apply {
            clear()
            addAll(data)
        }
    }

    fun setOnActionEditTeamListner(callback:(ModelTeam)->Unit){
        this.action_edit_team = callback
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var teamName: TextView? = itemView.findViewById(R.id.teamName)
        var txt_total_player: TextView? = itemView.findViewById(R.id.txt_total_player)
        var img_edit: ImageView? = itemView.findViewById(R.id.img_edit)
        var iv_team_profile: CircleImageView? = itemView.findViewById(R.id.iv_team_profile)
    }
}