package com.project.brandi.ui.product

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.project.brandi.App
import com.project.brandi.R
import com.project.brandi.data.order.Order
import com.project.brandi.data.product.ProductRepository
import com.project.brandi.util.Resource
import com.project.brandi.util.snackBar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class ProductFragment : Fragment(R.layout.fragment_product) {

    private lateinit var rootLayout: ConstraintLayout

    private lateinit var btnBuy: Button
    private lateinit var ivProduct: ImageView
    private lateinit var tvPrice: TextView
    private lateinit var tvMallName: TextView
    private lateinit var tvBrand: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvTitle: TextView

    private lateinit var product: JSONObject
    private lateinit var viewModel: ProductViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        product = JSONObject(arguments?.getSerializable("product").toString())

        val productRepository = ProductRepository()
        val viewModelFactory = ProductViewModelFactory(productRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProductViewModel::class.java)

        setFindViewById(view)
        setProduct(product)
        setOnClickListener()
        setOrderResponse()
    }

    private fun setOnClickListener() {
        btnBuy.setOnClickListener {
            val userId = App.prefs.getString("token")
            val productId = product["productId"].toString()
            val order = Order(null, userId.toString(), productId, null,null)
            val gson = Gson().toJson(order)
            Log.e("TAG", "setOnClickListener: ${gson}")
            viewModel.orderProduct(
                gson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            )
        }
    }

    private fun setFindViewById(view: View){
        rootLayout = view.findViewById(R.id.rootLayout)

        btnBuy = view.findViewById(R.id.btnBuy)
        ivProduct = view.findViewById(R.id.ivProduct)
        tvTitle = view.findViewById(R.id.tvTitle)
        tvPrice = view.findViewById(R.id.tvPrice)
        tvMallName = view.findViewById(R.id.tvMallName)
        tvBrand = view.findViewById(R.id.tvBrand)
        tvCategory = view.findViewById(R.id.tvCategory)
    }

    private fun setProduct(product: JSONObject){
        Glide.with(this)
            .load(product["image"].toString())
            .thumbnail(0.1f)
            .into(ivProduct)
        var title = product["title"].toString().replace("<b>", "")
        title = title.replace("<b>", "")
        title = title.replace("</b>", "")
        title = title.replace("/", "")
        tvTitle.text = title
        tvPrice.text = product["lprice"].toString()
        tvMallName.text = product["mallName"].toString()
        tvBrand.text = product["brand"].toString()
        tvCategory.text = product["category1"].toString()
    }

    private fun setOrderResponse() {
        viewModel.orderResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        rootLayout.snackBar(it.message)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
//        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
//        progressBar.visibility = View.VISIBLE
    }
}