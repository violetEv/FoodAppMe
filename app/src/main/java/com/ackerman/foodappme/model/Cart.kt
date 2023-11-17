package com.ackerman.foodappme.model

data class Cart(
    var id: Int? = null,
    var menuId: Int? = null,
    var itemQuantity: Int = 0,
    var itemNotes: String? = null,
    val menuName: String,
    val menuPrice: Double,
    val imgMenuUrl: String
)
