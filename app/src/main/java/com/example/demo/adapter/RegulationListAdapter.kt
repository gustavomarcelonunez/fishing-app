package com.example.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.example.demo.model.Regulation

class RegulationListAdapter: RecyclerView.Adapter<RegulationListAdapter.RegulationViewHolder>() {

    var regulations = listOf<Regulation>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegulationViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.regulation_item, parent, false)
        return RegulationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegulationViewHolder, position: Int) {
        val regulation = regulations[position]
        holder.bind(regulation)
    }

    class RegulationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.regulation_title)
        val description: TextView = view.findViewById(R.id.regulation_description)
        val zone: TextView = view.findViewById(R.id.regulation_zone)

        fun bind(regulation: Regulation) {
            title.text = regulation.title
            description.text = regulation.description
            zone.text = regulation.zone
        }
    }
    override fun getItemCount() = regulations.size
}