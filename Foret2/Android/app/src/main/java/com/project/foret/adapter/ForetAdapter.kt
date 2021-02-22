package com.project.foret.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.foret.R
import com.project.foret.model.Foret
import com.project.foret.util.Constants.Companion.BASE_URL
import java.text.SimpleDateFormat

class ForetAdapter : RecyclerView.Adapter<ForetAdapter.ForetViewHolder>() {
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
                R.layout.item_foret,
                parent,
                false
            )
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ForetViewHolder, position: Int) {
        val foret = differ.currentList[position]
        val format = SimpleDateFormat("yyyy-MM-dd")

        var tag = ""
        var region = ""

        if (foret.tags != null) {
            for (i in foret.tags) {
                tag += "#${i.tagName} "
            }
        }
        if( foret.regions != null) {
            for (i in foret.regions) {
                region += "${i.regionSi} ${i.regionGu}, "
            }
        }
        region = region.trim()
        region = region.substring(0, region.length-1)

        val reg_date: String = format.format(foret.reg_date)

        holder.itemView.apply {
            holder.itemView.findViewById<TextView>(R.id.tvForetName).text = foret.name
            holder.itemView.findViewById<TextView>(R.id.tvForetIntroduce).text = foret.introduce
            holder.itemView.findViewById<TextView>(R.id.tvForetTag).text = tag.trim()
            holder.itemView.findViewById<TextView>(R.id.tvForetRegion).text = region
            holder.itemView.findViewById<TextView>(R.id.tvForetRegDate).text = reg_date
            if (foret.photos != null) {
                Glide.with(this)
                    .load("${BASE_URL}${foret.photos[0].dir}/${foret.photos[0].filename}")
                    .thumbnail(0.1f)
                    .into(holder.itemView.findViewById(R.id.ivForetImage))
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