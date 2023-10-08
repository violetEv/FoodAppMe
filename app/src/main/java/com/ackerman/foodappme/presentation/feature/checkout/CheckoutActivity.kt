package com.ackerman.foodappme.presentation.feature.checkout


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ackerman.foodappme.data.local.database.AppDatabase
import com.ackerman.foodappme.data.local.database.datasource.CartDataSource
import com.ackerman.foodappme.data.local.database.datasource.CartDatabaseDataSource
import com.ackerman.foodappme.data.local.datastore.appDataStore
import com.ackerman.foodappme.data.repository.CartRepository
import com.ackerman.foodappme.data.repository.CartRepositoryImpl
import com.ackerman.foodappme.databinding.ActivityCheckoutBinding
import com.ackerman.foodappme.utils.GenericViewModelFactory
import com.ackerman.foodappme.utils.toCurrencyFormat

class CheckoutActivity : AppCompatActivity() {
    private val binding: ActivityCheckoutBinding by lazy {
        ActivityCheckoutBinding.inflate(layoutInflater)
    }
    private val viewModel: CheckoutViewModel by viewModels {
        val database = AppDatabase.getInstance(this)
        val cartDao = database.cartDao()
        val cartDataSource: CartDataSource = CartDatabaseDataSource(cartDao)
        val repo: CartRepository = CartRepositoryImpl(cartDataSource)
        GenericViewModelFactory.create(
            CheckoutViewModel(repo)
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeData()
    }


    private fun observeData() {
        viewModel.cartList.observe(this) { resultData ->
            resultData.payload?.let { (_, totalPrice) ->
                binding.layoutState.tvTotalPayment.text = totalPrice.toCurrencyFormat()
            }
        }
    }

}
