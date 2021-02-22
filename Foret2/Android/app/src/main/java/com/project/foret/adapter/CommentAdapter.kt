package com.project.foret.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.foret.R
import com.project.foret.model.Comment

class CommentAdapter(val isAnonymous: Boolean, val member_id: Long) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<Comment>() {

        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_comment,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {

        val comment = differ.currentList[position]

        if(comment.id != comment.group_id){
            val layoutRoot = holder.itemView.findViewById<LinearLayout>(R.id.layoutRoot)
            val layoutParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParam.setMargins(48,0,0,0)
            layoutRoot.layoutParams = layoutParam
        }


        holder.itemView.apply {
            if (isAnonymous)
                this.findViewById<TextView>(R.id.tvCommentWriter).text =
                    comment.member?.nickname
            else
                this.findViewById<TextView>(R.id.tvCommentWriter).text =
                    comment.member?.name
            this.findViewById<TextView>(R.id.tvCommentContent).text = comment.content
            this.findViewById<TextView>(R.id.tvCommentRegDate).text =
                comment.reg_date?.substring(0, comment.reg_date.indexOf("T"))
            this.findViewById<TextView>(R.id.tvReComment).setOnClickListener {
                onClickListener.onReCommentClick(it, position)
            }
            this.findViewById<TextView>(R.id.tvEditComment).setOnClickListener {
                onClickListener.onEditCommentClick(it, position)
            }
            this.findViewById<TextView>(R.id.tvDeleteComment).setOnClickListener {
                onClickListener.onDeleteCommentClick(it, position)
            }
            if(member_id != comment.member?.id){
                this.findViewById<TextView>(R.id.tvEditComment).visibility = View.INVISIBLE
                this.findViewById<TextView>(R.id.tvDeleteComment).visibility = View.INVISIBLE
            }
        }
    }

    interface OnClickListener {
        fun onReCommentClick(v: View, position: Int)        // == onClickListener
        fun onEditCommentClick(v: View, position: Int)
        fun onDeleteCommentClick(v: View, position: Int)
    }

    private lateinit var onClickListener: OnClickListener

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    override fun getItemCount(): Int = differ.currentList.size

}