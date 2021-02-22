package com.project.foret.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.foret.R
import com.project.foret.model.Board
import com.project.foret.model.Photo
import com.project.foret.util.Constants.Companion.BASE_URL

class BoardImageAdapter : RecyclerView.Adapter<BoardImageAdapter.BoardViewHolder>() {
    inner class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        return BoardViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_board_image,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val photo = differ.currentList[position]
        holder.itemView.apply {
            if (photo != null) {
                Glide.with(this)
                    .load("${BASE_URL}${photo.dir}/${photo.filename}")
                    .error(R.drawable.icon_null_image)
                    .into(holder.itemView.findViewById(R.id.ivBoardImage))
            } else {
                Glide.with(this)
                    .load(R.drawable.icon_null_image)
                    .into(holder.itemView.findViewById(R.id.ivBoardImage))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Board) -> Unit)? = null

    fun setOnItemClickListener(listener: (Board) -> Unit) {
        onItemClickListener = listener
    }
}
