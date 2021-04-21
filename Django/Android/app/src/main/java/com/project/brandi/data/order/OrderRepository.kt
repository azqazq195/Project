package com.project.brandi.data.order

import com.project.brandi.api.RetrofitInstance
import okhttp3.RequestBody


class OrderRepository {
    suspend fun getOrder(token: String) =
        RetrofitInstance.api.getOrder(token)
}