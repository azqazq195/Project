package com.project.brandi.data.product

import com.project.brandi.api.RetrofitInstance
import okhttp3.RequestBody

class ProductRepository {
    suspend fun getProduct() =
        RetrofitInstance.api.getProduct()

    suspend fun purchaseProduct(order: RequestBody) =
        RetrofitInstance.api.orderProduct(order)
}