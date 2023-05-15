package com.diyantech.dreamcricket.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diyantech.dreamcricket.R
import com.diyantech.dreamcricket.model.ModelMatch

class MatchAdapter : RecyclerView.Adapter<MatchAdapter.Holder>() {
    private var matchList = mutableListOf<ModelMatch>()
    private var action_edit : ((ModelMatch) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_add_match, parent, false)
        return MatchAdapter.Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val match = matchList[position]

        holder.txtMatch?.text = match.title
        holder.team_one?.text = match.teamone
        holder.team_two?.text = match.teamtwo
        holder.txt_Date?.text = match.date
        holder.txt_time?.text = match.time

        holder.action_edit?.setOnClickListener {
            action_edit?.invoke(match)
        }

    }

    override fun getItemCount(): Int {
        return matchList.size
    }

    fun setData(data : List<ModelMatch>){
        matchList.apply {
            clear()
            addAll(data)
        }
    }

    fun setOnActionEditListner(callback:(ModelMatch)->Unit){
        this.action_edit = callback
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtMatch: TextView? = itemView.findViewById(R.id.matchName)
        var team_one: TextView? = itemView.findViewById(R.id.team_one)
        var team_two: TextView? = itemView.findViewById(R.id.team_two)
        var txt_Date: TextView? = itemView.findViewById(R.id.txt_Date)
        var txt_time: TextView? = itemView.findViewById(R.id.txt_time)
        var action_edit : ImageView? = itemView.findViewById(R.id.action_edit)
    }
}