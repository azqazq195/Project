package com.project.brandi.ui.home

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
import com.project.brandi.data.product.Product


class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_product,
                parent,
                false
            )
        )
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        var title = product.title.toString().replace("<b>", "")
        title = title.replace("<b>", "")
        title = title.replace("</b>", "")
        title = title.replace("/", "")

        holder.itemView.apply {
            holder.itemView.findViewById<TextView>(R.id.tvProductName).text = title
            holder.itemView.findViewById<TextView>(R.id.tvProductCategory).text = product.category1
            holder.itemView.findViewById<TextView>(R.id.tvProductBrand).text = product.brand
            holder.itemView.findViewById<TextView>(R.id.tvProductPrice).text = product.lprice
            if (product.image != null) {
                Glide.with(this)
                    .load(product.image)
                    .thumbnail(0.1f)
                    .into(holder.itemView.findViewById(R.id.ivProductImage))
            }
            setOnClickListener{
                onItemClickListener?.let { it(product)}
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Product) -> Unit)? = null

    fun setOnItemClickListener(listener: (Product) -> Unit) {
        onItemClickListener = listener
    }
}