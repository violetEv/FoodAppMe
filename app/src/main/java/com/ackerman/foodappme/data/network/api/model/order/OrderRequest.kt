package com.ackerman.foodappme.data.network.api.model.order


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class OrderRequest(
    @SerializedName("orders")
    val orders: List<OrderItemRequest>?
)