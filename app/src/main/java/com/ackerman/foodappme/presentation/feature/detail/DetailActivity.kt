package com.ackerman.foodappme.presentation.feature.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.ackerman.foodappme.data.local.database.AppDatabase
import com.ackerman.foodappme.data.local.database.datasource.CartDataSource
import com.ackerman.foodappme.data.local.database.datasource.CartDatabaseDataSource
import com.ackerman.foodappme.data.network.api.datasource.FoodAppApiDataSource
import com.ackerman.foodappme.data.network.api.service.FoodAppApiService
import com.ackerman.foodappme.data.repository.CartRepository
import com.ackerman.foodappme.data.repository.CartRepositoryImpl
import com.ackerman.foodappme.databinding.ActivityDetailBinding
import com.ackerman.foodappme.model.Menu
import com.ackerman.foodappme.utils.GenericViewModelFactory
import com.ackerman.foodappme.utils.proceedWhen
import com.ackerman.foodappme.utils.toCurrencyFormat

class DetailActivity : AppCompatActivity() {

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private val viewModel: DetailViewModel by viewModels {
        val database = AppDatabase.getInstance(this)
        val cartDao = database.cartDao()
        val cartDataSource: CartDataSource = CartDatabaseDataSource(cartDao)
        val service = FoodAppApiService.invoke()
        val apiDataSource = FoodAppApiDataSource(service)
        val repo: CartRepository = CartRepositoryImpl(cartDataSource,apiDataSource)
        GenericViewModelFactory.create(
            DetailViewModel(intent?.extras, repo)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindMenu(viewModel.menu)
        observeData()
        setCLick()
        setClickListener()
    }

    private fun setCLick() {
        binding.clLocation.setOnClickListener{
            navigateToGoogleMaps()
        }
    }

    private fun navigateToGoogleMaps() {
        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:-6.300550266059584, 106.65447133997473"))
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    private fun setClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.ivMinus.setOnClickListener {
            viewModel.minus()
        }
        binding.ivPlus.setOnClickListener {
            viewModel.add()
        }
        binding.btnAddToCart.setOnClickListener {
            viewModel.addToCart()
        }

    }

    private fun observeData() {
        viewModel.priceLiveData.observe(this) {
            binding.tvTotalPrice.text = it.toCurrencyFormat()
        }
        viewModel.menuCountLiveData.observe(this) {
            binding.tvMenuCount.text = it.toString()
        }
        viewModel.addToCartResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(this, "Add to cart success !", Toast.LENGTH_SHORT).show()
                    finish()
                }, doOnError = {
                    Toast.makeText(this, it.exception?.message.orEmpty(), Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun bindMenu(menu: Menu?) {
        menu?.let { item ->
            binding.ivMenuImage.load(item.imgMenuUrl) {
                crossfade(true)
            }
            binding.tvMenuName.text = item.name
            binding.tvMenuDesc.text = item.desc
            binding.tvMenuPrice.text = item.price.toCurrencyFormat()
        }
    }

    companion object {
        const val EXTRA_MENU= "EXTRA_MENU"
        fun startActivity(context: Context, product: Menu) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_MENU, product)
            context.startActivity(intent)
        }
    }
}