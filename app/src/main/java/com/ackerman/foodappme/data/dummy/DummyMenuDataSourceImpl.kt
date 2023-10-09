package com.ackerman.foodappme.data.dummy

import com.ackerman.foodappme.model.Menu

interface DummyMenuDataSource {
    fun getMenuList() : List<Menu>
}
class DummyMenuDataSourceImpl() : DummyMenuDataSource {
    override fun getMenuList(): List<Menu> = listOf(
        Menu(
            id = 1,
            name = "Grilled Chicken",
            price = 50000.0,
            imgMenuUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/menu_image/img_menu_grilled_chicken.jpg",
            desc = "Chicken pieces grilled with signature spices, savory, and often served with rice or side vegetables."
        ),
        Menu(
            id = 2,
            name = "Ramen",
            price = 30000.0,
            imgMenuUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/menu_image/img_menu_ramen.jpg",
            desc = "Japanese noodles served in a flavorful broth with various toppings such as meat, eggs, and seaweed."
        ),
        Menu(
            id = 3,
            name = "Satay",
            price = 20000.0,
            imgMenuUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/menu_image/img_menu_satay.jpg",
            desc = "Skewered chicken pieces grilled with peanut sauce, served with rice or compressed rice cake, featuring a sweet, salty, and spicy flavor."
        ),
        Menu(
            id = 4,
            name = "Fried Chicken",
            price = 40000.0,
            imgMenuUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/menu_image/img_menu_fried_chicken.jpg",
            desc = "Chicken fried to crispy perfection with a crunchy skin, typically served with rice or french fries."
        ),
        Menu(
            id = 5,
            name = "Cheese Burger",
            price = 30000.0,
            imgMenuUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/menu_image/img_menu_cheese_burger.jpg",
            desc = "A sandwich consisting of a beef patty, cheese, vegetables, and condiments, available in various flavor combinations and toppings."
        ),
        Menu(
            id = 6,
            name = "Pizza",
            price = 45000.0,
            imgMenuUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/menu_image/img_menu_pizza.jpg",
            desc = "Pizza dough with an array of toppings such as cheese, tomato sauce, and various other ingredients, a popular choice for sharing."
        ),
        Menu(
            id = 7,
            name = "French Fries",
            price = 50000.0,
            imgMenuUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/menu_image/img_menu_french_fries.webp",
            desc = "Thinly cut and deep-fried potato sticks, often served with ketchup or mayonnaise, providing a crispy snack."
        ),
        Menu(
            id = 8,
            name = "Strawberry Milk",
            price = 15000.0,
            imgMenuUrl = "https://raw.githubusercontent.com/violetEv/CH4-asset-code-challenge/main/menu_image/img_menu_strawberry_milk.jpg",
            desc = "A refreshing and sweet milk-based beverage infused with the flavor of ripe strawberries, commonly enjoyed as a dessert drink."
        )
    )

}