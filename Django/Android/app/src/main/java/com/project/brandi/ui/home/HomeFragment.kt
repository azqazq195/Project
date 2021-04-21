package com.project.brandi.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.project.brandi.R
import com.project.brandi.data.product.ProductRepository
import com.project.brandi.util.Resource


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var viewModel: HomeViewModel
    private lateinit var productAdapter: ProductAdapter

    private lateinit var rvProduct: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productRepository = ProductRepository()
        val viewModelFactory = HomeViewModelFactory(productRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        viewModel.getProduct()

        setFindViewById(view)
        setProductResponse()
        setUpRecyclerView()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        productAdapter.setOnItemClickListener {
            val bundle = bundleOf("product" to Gson().toJson(it))
            view?.findNavController()
                ?.navigate(
                    R.id.action_homeFragment_to_productFragment,
                    bundle
                )
        }
    }

    private fun setFindViewById(view: View) {
        rvProduct = view.findViewById(R.id.rvProduct)
    }

    private fun setUpRecyclerView() {
        productAdapter = ProductAdapter()
        rvProduct.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setProductResponse() {
        viewModel.products.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        productAdapter.differ.submitList(it.products)
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