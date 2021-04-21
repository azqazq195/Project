package com.project.brandi.ui.order

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.brandi.R
import com.project.brandi.data.order.Order
import com.project.brandi.data.product.Product


class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_order,
                parent,
                false
            )
        )
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = differ.currentList[position]

        holder.itemView.apply {
            holder.itemView.findViewById<TextView>(R.id.tvId).text = "주문 번호 ${order.id}"
            holder.itemView.findViewById<TextView>(R.id.tvProductId).text = "상품 번호 ${order.productId}"
            holder.itemView.findViewById<TextView>(R.id.tvOrderDate).text = "주문 날짜 ${order.orderDate}"
            holder.itemView.findViewById<TextView>(R.id.tvStatus).text = order.status
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}