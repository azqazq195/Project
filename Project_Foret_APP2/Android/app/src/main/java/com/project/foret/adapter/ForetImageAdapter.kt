package com.project.foret.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.foret.R
import com.project.foret.model.Foret
import com.project.foret.util.Constants.Companion.BASE_URL

class ForetImageAdapter : RecyclerView.Adapter<ForetImageAdapter.ForetViewHolder>() {
    inner class ForetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<Foret>() {
        override fun areItemsTheSame(oldItem: Foret, newItem: Foret): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Foret, newItem: Foret): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForetViewHolder {
        return ForetViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_foret_image,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ForetViewHolder, position: Int) {
        val foret = differ.currentList[position]
        holder.itemView.apply {
            if (foret.photos != null) {
                Glide.with(this)
                    .load("${BASE_URL}${foret.photos[0].dir}/${foret.photos[0].filename}")
                    .error(R.drawable.icon_null_image)
                    .into(holder.itemView.findViewById(R.id.ivItemForetImage))
            } else {
                Glide.with(this)
                    .load(R.drawable.icon_null_image)
                    .into(holder.itemView.findViewById(R.id.ivItemForetImage))
            }

            setOnClickListener {
                onItemClickListener?.let { it(foret) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Foret) -> Unit)? = null

    fun setOnItemClickListener(listener: (Foret) -> Unit) {
        onItemClickListener = listener
    }
}