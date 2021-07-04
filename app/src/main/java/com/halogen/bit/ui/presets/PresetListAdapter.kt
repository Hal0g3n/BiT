package com.halogen.bit.ui.presets

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.halogen.bit.R
import kotlinx.android.synthetic.main.preset_card.view.*

class PresetListAdapter(
    private val fragmentListener: PresetsFragment,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder

        val plans = fragmentListener.databaseManager.plans
        holder.itemTitle.text = plans[position].name
        holder.itemDuration.text = plans[position].duration.toString()

        holder.itemView.transitionName = "view$position"
    }

    override fun getItemCount(): Int {
        return fragmentListener.databaseManager.plans.size
    }

    class ViewHolder(item: View, private val listener: PresetsFragment): RecyclerView.ViewHolder(item) {
        val itemTitle: TextView = item.item_title
        val itemDuration: TextView = item.item_duration

        init {

            item.setOnClickListener{ listener.onItemClick(adapterPosition, item); }

            item.setOnLongClickListener { listener.onItemLongClick(adapterPosition, item); true }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.preset_card, parent, false)
        return ViewHolder(v, fragmentListener)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View)
        fun onItemLongClick(position: Int, view: View)
    }
}