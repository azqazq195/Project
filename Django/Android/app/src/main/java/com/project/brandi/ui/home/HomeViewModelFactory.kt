package com.project.brandi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.brandi.data.product.ProductRepository
import com.project.brandi.data.user.UserRepository
import com.project.brandi.ui.entrance.EntranceViewModel

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val productRepository: ProductRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(productRepository) as T
    }
}