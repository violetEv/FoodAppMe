package com.ackerman.foodappme.model

import java.util.UUID

data class Category(
    val id: String = UUID.randomUUID().toString(),
    val imgCategoryUrl: String,
    val slug: String?,
    val name: String
)