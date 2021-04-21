package com.project.brandi.ui.order

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.brandi.App
import com.project.brandi.R
import com.project.brandi.data.order.OrderRepository
import com.project.brandi.data.user.UserRepository
import com.project.brandi.ui.entrance.EntranceViewModel
import com.project.brandi.ui.entrance.EntranceViewModelFactory
import com.project.brandi.ui.home.HomeViewModel
import com.project.brandi.ui.home.ProductAdapter
import com.project.brandi.util.Resource

class OrderFragment : Fragment(R.layout.fragment_order) {

    private lateinit var viewModel: OrderViewModel
    private lateinit var orderAdapter: OrderAdapter

    private lateinit var rvOrder: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderRepository = OrderRepository()
        val viewModelFactory = OrderViewModelFactory(orderRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(OrderViewModel::class.java)

        viewModel.getOrder(App.prefs.getString("token").toString())

        setFindViewById(view)
        setUpRecyclerView()
        setOrderResponse()
    }

    private fun setFindViewById(view: View) {
        rvOrder = view.findViewById(R.id.rvOrder)
    }

    private fun setUpRecyclerView() {
        orderAdapter = OrderAdapter()
        rvOrder.apply {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun setOrderResponse() {
        viewModel.orders.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        orderAdapter.differ.submitList(it.orders)
                        Log.e("TAG", "setOrderResponse: DONE => ${it.orders}", )
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