package com.ackerman.foodappme.presentation.feature.checkout


import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ackerman.foodappme.data.repository.CartRepository
import kotlinx.coroutines.Dispatchers

class CheckoutViewModel(private val repository: CartRepository) : ViewModel() {

    val cartList = repository.getCartList().asLiveData(Dispatchers.IO)

}