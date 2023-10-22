package com.ackerman.foodappme.data.dummy

import com.ackerman.foodappme.model.Category

interface DummyCategoryDataSource {
    fun getMenuCategory(): List<Category>
}

class DummyCategoryDataSourceImpl() : DummyCategoryDataSource {
    override fun getMenuCategory(): List<Category> =
        listOf(
            Category(
                name = "All",
                slug = "all",
                imgCategoryUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/categories/ic_all_category.png"
            ),
            Category(
                name = "Rice",
                slug = "rice",
                imgCategoryUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/categories/ic_category_rice.png"
            ),
            Category(
                name = "Noodle",
                slug = "noodle",
                imgCategoryUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/categories/ic_category_noodle.png"
            ),
            Category(
                name = "Snack",
                slug = "snack",
                imgCategoryUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/categories/ic_category_snack.png"
            ),
            Category(
                name = "Beverages",
                slug = "beverages",
                imgCategoryUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/categories/ic_category_beverages.png"
            ),
            Category(
                name = "Meat",
                slug = "meat",
                imgCategoryUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/categories/ic_category_meat.png"
            ),
            Category(
                name = "Cake",
                slug = "cake",
                imgCategoryUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/categories/ic_category_cake.png"
            ),
        )
}