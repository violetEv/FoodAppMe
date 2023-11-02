package com.ackerman.foodappme.model

import com.ackerman.foodappme.data.network.api.model.order.OrderItemRequest

data class OrderItem(
    val notes: String,
    val productId: Int,
    val qty: Int
)

fun OrderItem.toOrderItemRequest() = OrderItemRequest(notes, productId, qty)
