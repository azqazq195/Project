package com.project.vllo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.vllo.R
import com.project.vllo.model.Item

class GalleryAdapter(
    private val context: Context,
    private val itemList: MutableList<Item>,
    private val itemListener: ItemListener
) : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
    inner class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val item = itemList[position]
        val ivImage = holder.itemView.findViewById<ImageView>(R.id.ivImage)
        val tvInfo = holder.itemView.findViewById<TextView>(R.id.tvInfo)

        when (item.mimeType) {
            "image/gif" -> {
                Glide.with(context).asGif().load(item.uri).into(ivImage)
                tvInfo.visibility = View.VISIBLE
                tvInfo.text = "GIF"
            }
            "video/mp4" -> {
                item.duration!!
                val minute = item.duration / 60000
                val second = (item.duration - (item.duration / 60000 * 60000)) / 1000

                Glide.with(context).load(item.uri).into(ivImage)
                tvInfo.visibility = View.VISIBLE
                tvInfo.text = String.format("%02d:%02d", minute, second)
            }
            else -> {
                Glide.with(context).load(item.uri).into(ivImage)
            }
        }

        holder.itemView.setOnClickListener {
            itemListener.onItemClick(item)
        }
    }

    override fun getItemCount() = itemList.size

    interface ItemListener {
        fun onItemClick(item: Item)
    }
}