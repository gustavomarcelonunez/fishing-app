package com.example.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demo.R
import com.example.demo.model.Contest

class ContestListAdapter(
    private val itemClickListener: OnContestClickListener
): RecyclerView.Adapter<ContestListAdapter.ContestViewHolder>() {

    var contests = listOf<Contest>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContestViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.contest_item, parent, false)
        return ContestViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContestViewHolder, position: Int) {
        val contest = contests[position]
        holder.bind(contest)
    }

    inner class ContestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.contest_title)
        val description: TextView = view.findViewById(R.id.contest_description)
        val featuredImage: ImageView = view.findViewById(R.id.featured_image)

        fun bind(contest: Contest) {
            itemView.setOnClickListener { itemClickListener.onItemClick(contest) }
            title.text = contest.title
            description.text = contest.description
            featuredImage.setImageResource(contest.featuredImage)
            featuredImage.scaleType
        }
    }

    override fun getItemCount() = contests.size

    interface OnContestClickListener {
        fun onItemClick(contest: Contest)
    }
}
