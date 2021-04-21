package com.project.brandi.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.brandi.data.order.OrderRepository


@Suppress("UNCHECKED_CAST")
class OrderViewModelFactory(
    private val orderRepository: OrderRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OrderViewModel(orderRepository) as T
    }
}