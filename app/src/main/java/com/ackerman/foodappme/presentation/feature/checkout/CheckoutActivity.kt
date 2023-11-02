package com.ackerman.foodappme.presentation.feature.checkout

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ackerman.foodappme.R
import com.ackerman.foodappme.databinding.ActivityCheckoutBinding
import com.ackerman.foodappme.presentation.common.CartListAdapter
import com.ackerman.foodappme.utils.proceedWhen
import com.ackerman.foodappme.utils.toCurrencyFormat
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckoutActivity : AppCompatActivity() {

    private val viewModel: CheckoutViewModel by viewModel()

    private val binding: ActivityCheckoutBinding by lazy {
        ActivityCheckoutBinding.inflate(layoutInflater)
    }

    private val adapter: CartListAdapter by lazy {
        CartListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupList()
        observeData()
        setClickListener()
    }

    private fun observeData() {
        observeCartData()
        observeCheckoutResult()
    }

    private fun observeCheckoutResult() {
        viewModel.checkoutResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    showSuccessDialog()
                },
                doOnError = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    Toast.makeText(this, "Checkout Error", Toast.LENGTH_SHORT).show()
                },
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                }
            )
        }
    }

    private fun setClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnCheckout.setOnClickListener {
            viewModel.order()
        }
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.text_checkout_success))
            .setPositiveButton(getString(R.string.text_okay)) { _, _ ->
                viewModel.clearCart()
                finish()
            }.create().show()
    }

    private fun setupList() {
        binding.layoutContent.rvCart.adapter = adapter
    }

    private fun observeCartData() {
        viewModel.cartList.observe(this) {
            it.proceedWhen(doOnSuccess = { result ->
                binding.layoutState.root.isVisible = false
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = false
                binding.layoutContent.root.isVisible = true
                binding.layoutContent.rvCart.isVisible = true
                binding.clSectionOrder.isVisible = true
                result.payload?.let { (carts, totalPrice) ->
                    adapter.submitData(carts)
                    binding.layoutContent.tvItemPrice.text = totalPrice.toCurrencyFormat()
                }
            }, doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvCart.isVisible = false
                    binding.clSectionOrder.isVisible = false
                }, doOnError = { err ->
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = err.exception?.message.orEmpty()
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvCart.isVisible = false
                    binding.clSectionOrder.isVisible = false
                }, doOnEmpty = { data ->
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = getString(R.string.text_cart_is_empty)
                    data.payload?.let { (_, totalPrice) ->
                        binding.layoutContent.tvItemPrice.text = totalPrice.toCurrencyFormat()
                    }
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvCart.isVisible = false
                    binding.clSectionOrder.isVisible = false
                })
        }
    }
}
