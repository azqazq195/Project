package com.project.foret.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.foret.R
import com.project.foret.model.Board


class AnonymousBoardAdapter : RecyclerView.Adapter<AnonymousBoardAdapter.AnonymousBoardViewHolder>() {
    inner class AnonymousBoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<Board>() {
        override fun areItemsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnonymousBoardAdapter.AnonymousBoardViewHolder {
        return AnonymousBoardViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_anonymous_board,
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: AnonymousBoardAdapter.AnonymousBoardViewHolder,
        position: Int
    ) {
        val board = differ.currentList[position]
        holder.itemView.apply {
            val spannable1 = SpannableStringBuilder("공감 (${board.like_count})")
            spannable1.setSpan(
                ForegroundColorSpan(Color.parseColor("#FF22997b")),
                4, // start
                spannable1.length-1, // end
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
            spannable1.setSpan(
                StyleSpan(Typeface.BOLD),
                4, // start
                spannable1.length-1, // end
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )

            val spannable2 = SpannableStringBuilder("댓글 (${board.comment_count})")
            spannable2.setSpan(
                ForegroundColorSpan(Color.parseColor("#FF22997b")),
                4, // start
                spannable2.length-1, // end
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
            spannable2.setSpan(
                StyleSpan(Typeface.BOLD),
                4, // start
                spannable2.length-1, // end
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )

            holder.itemView.findViewById<TextView>(R.id.tvAnonySubject).text = board.subject
            holder.itemView.findViewById<TextView>(R.id.tvAnonyWriter).text = board.member?.nickname
            holder.itemView.findViewById<TextView>(R.id.tvAnonyContent).text = board.content
            holder.itemView.findViewById<TextView>(R.id.tvAnonyLike).text = spannable1
            holder.itemView.findViewById<TextView>(R.id.tvAnonyComment).text = spannable2
            holder.itemView.findViewById<TextView>(R.id.tvAnonyRegDate).text =
                board.reg_date?.substring(0, board.reg_date.indexOf("T"))
            holder.itemView.findViewById<ToggleButton>(R.id.button_like).isChecked = true
            setOnClickListener {
                onItemClickListener?.let { it(board) }
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