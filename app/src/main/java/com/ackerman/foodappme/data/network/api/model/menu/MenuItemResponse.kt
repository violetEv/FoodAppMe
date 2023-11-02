package com.ackerman.foodappme.data.network.api.model.menu

import androidx.annotation.Keep
import com.ackerman.foodappme.model.Menu
import com.google.gson.annotations.SerializedName

@Keep
data class MenuItemResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("address")
    val resAddress: String?,
    @SerializedName("format_price")
    val formatPrice: String?,
    @SerializedName("desc")
    val desc: String?,
    @SerializedName("img_menu_url")
    val imgMenuUrl: String?
)

fun MenuItemResponse.toMenu() = Menu(
    id = this.id ?: 0,
    name = this.name.orEmpty(),
    price = this.price ?: 0.0,
    desc = this.desc.orEmpty(),
    imgMenuUrl = this.imgMenuUrl.orEmpty()
)

fun Collection<MenuItemResponse>.toMenuList() = this.map { it.toMenu() }
