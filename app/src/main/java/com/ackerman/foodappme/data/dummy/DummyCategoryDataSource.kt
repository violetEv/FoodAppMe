package com.ackerman.foodappme.data.dummy

import com.ackerman.foodappme.model.Category

interface DummyCategoryDataSource {
    fun getMenuCategory(): List<Category>
}

class DummyCategoryDataSourceImpl() : DummyCategoryDataSource {
    override fun getMenuCategory(): List<Category> =
        listOf(
            Category(
                name = "Rice",
                imgCategoryUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/categories/ic_category_rice.png"
            ),
            Category(
                name = "Noodle",
                imgCategoryUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/categories/ic_category_noodle.png"
            ),
            Category(
                name = "Snack",
                imgCategoryUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/categories/ic_category_snack.png"
            ),
            Category(
                name = "Beverages",
                imgCategoryUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/categories/ic_category_beverages.png"
            ),
            Category(
                name = "Meat",
                imgCategoryUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/categories/ic_category_meat.png"
            ),
            Category(
                name = "Cake",
                imgCategoryUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/categories/ic_category_cake.png"
            ),
        )
}