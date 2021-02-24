package com.project.vllo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.vllo.R

class ImageAdapter(
    private val context: Context,
    private val imageList: ArrayList<String>,
    private val imageListener: ImageListener
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = imageList[position]
        val ivImage = holder.itemView.findViewById<ImageView>(R.id.ivImage)

        Glide.with(context).load(image).into(ivImage)

        holder.itemView.setOnClickListener {
            imageListener.onImageClick(image)
        }
    }

    override fun getItemCount() = imageList.size

    interface ImageListener {
        fun onImageClick(path: String)
    }
}