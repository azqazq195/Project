package com.project.brandi.ui.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.brandi.data.order.PurchaseResponse
import com.project.brandi.data.product.ProductRepository
import com.project.brandi.util.Resource
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response


class ProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    val orderResponse: MutableLiveData<Resource<PurchaseResponse>> = MutableLiveData()

    fun orderProduct(order: RequestBody) = viewModelScope.launch {
        orderResponse.postValue(Resource.Loading())
        val response = productRepository.purchaseProduct(order)
        orderResponse.postValue(handleOrderProductResponse(response))
    }

    private fun handleOrderProductResponse(response: Response<PurchaseResponse>) : Resource<PurchaseResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}