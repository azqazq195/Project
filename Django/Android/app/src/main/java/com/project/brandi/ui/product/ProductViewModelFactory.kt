package com.project.brandi.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.brandi.data.product.ProductRepository

@Suppress("UNCHECKED_CAST")
class ProductViewModelFactory(
    private val productRepository: ProductRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductViewModel(productRepository) as T
    }
}