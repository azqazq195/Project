package com.project.brandi.ui.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.brandi.data.order.OrderRepository
import com.project.brandi.data.order.OrderResponse
import com.project.brandi.data.user.CreateResponse
import com.project.brandi.data.user.LoginResponse
import com.project.brandi.data.user.UserRepository
import com.project.brandi.util.Resource
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response


class OrderViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    val orders: MutableLiveData<Resource<OrderResponse>> = MutableLiveData()

    fun getOrder(token: String) = viewModelScope.launch {
        orders.postValue(Resource.Loading())
        val response = orderRepository.getOrder(token)
        orders.postValue(handleGetOrderResponse(response))
    }

    private fun handleGetOrderResponse(response: Response<OrderResponse>) : Resource<OrderResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}