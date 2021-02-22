package com.project.flo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.project.flo.R
import com.project.flo.model.Lyrics

class LyricsAdapter(
    private val lyricsList: List<Lyrics>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<LyricsAdapter.LyricsViewHolder>() {

    inner class LyricsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val tvLyrics: TextView = itemView.findViewById(R.id.tvLyrics)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LyricsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_lyrics, parent, false)
        return LyricsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LyricsViewHolder, position: Int) {
        val currentItem = lyricsList[position]
        holder.setIsRecyclable(false)
        holder.tvLyrics.text = currentItem.lyrics
        if(currentItem.isNow){
            holder.tvLyrics.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
            ))
        }
    }

    override fun getItemCount() = lyricsList.size


}