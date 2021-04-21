package com.project.brandi.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.brandi.data.product.ProductRepository
import com.project.brandi.data.product.ProductResponse
import com.project.brandi.data.user.CreateResponse
import com.project.brandi.data.user.LoginResponse
import com.project.brandi.data.user.UserRepository
import com.project.brandi.util.Resource
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response


class HomeViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    val products: MutableLiveData<Resource<ProductResponse>> = MutableLiveData()

    fun getProduct() = viewModelScope.launch {
        products.postValue(Resource.Loading())
        val response = productRepository.getProduct()
        products.postValue(handleGetProductResponse(response))
    }

    private fun handleGetProductResponse(response: Response<ProductResponse>) : Resource<ProductResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}