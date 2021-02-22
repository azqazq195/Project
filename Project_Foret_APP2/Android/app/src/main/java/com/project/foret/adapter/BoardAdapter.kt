package com.project.foret.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.foret.R
import com.project.foret.model.Board

class BoardAdapter : RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {
    inner class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<Board>() {
        override fun areItemsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        return BoardViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_board,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val board = differ.currentList[position]
        holder.itemView.apply {
            holder.itemView.findViewById<TextView>(R.id.tvItemForetSubject).text = board.subject
            holder.itemView.findViewById<TextView>(R.id.tvItemForetContent).text = board.content
            holder.itemView.findViewById<TextView>(R.id.tvItemForetRegDate).text = board.reg_date?.substring(0, board.reg_date.indexOf("T"))
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