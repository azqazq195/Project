package com.project.vllo.edit

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.vllo.customview.TimeLineView
import com.project.vllo.databinding.ItemVideoTimelineBinding


class EditAdapter (private var thumbnailList : ArrayList<ArrayList<Bitmap>>, private var context: Context)
    : RecyclerView.Adapter<EditAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val mBinding = ItemVideoTimelineBinding.inflate(inflater, parent, false)
        return ViewHolder(mBinding, mBinding.itemVideo)
    }

    override fun getItemCount(): Int = thumbnailList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.drawView(thumbnailList[position])
    }


    inner class ViewHolder(
        mBinding: ItemVideoTimelineBinding,
        val image : TimeLineView
    ) : RecyclerView.ViewHolder(mBinding.root)
}