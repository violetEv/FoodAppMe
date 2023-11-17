package com.ackerman.foodappme.presentation.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ackerman.foodappme.R
import com.ackerman.foodappme.data.dummy.DummyMenuDataSource
import com.ackerman.foodappme.data.dummy.DummyMenuDataSourceImpl
import com.ackerman.foodappme.databinding.FragmentHomeBinding
import com.ackerman.foodappme.model.Menu
import com.ackerman.foodappme.presentation.feature.detail.DetailActivity
import com.ackerman.foodappme.presentation.feature.home.adapter.AdapterLayoutMode
import com.ackerman.foodappme.presentation.feature.home.adapter.CategoryListAdapter
import com.ackerman.foodappme.presentation.feature.home.adapter.MenuListAdapter
import com.ackerman.foodappme.presentation.feature.settings.SettingsDialogFragment
import com.ackerman.foodappme.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val dataSourceMenu: DummyMenuDataSource by lazy {
        DummyMenuDataSourceImpl()
    }
    private val adapterMenu: MenuListAdapter by lazy {
        MenuListAdapter(AdapterLayoutMode.LINEAR) {
            navigateToDetail(it)
        }
    }
    private val adapterCategory: CategoryListAdapter by lazy {
        CategoryListAdapter {
            viewModel.getMenus(it.slug)
        }
    }

    private fun navigateToDetail(menu: Menu) {
        DetailActivity.startActivity(requireContext(), menu)
    }

    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupSwitch()
        getData()
        observeData()
        showUserData()
        setClickListener()
    }

    private fun setClickListener() {
        binding.ivMore.setOnClickListener {
            Toast.makeText(requireContext(), "Setting Opened", Toast.LENGTH_SHORT).show()
            openSettingDialog()
        }
    }

    private fun openSettingDialog() {
        SettingsDialogFragment().show(childFragmentManager, null)
    }

    private fun getData() {
        viewModel.categories
        viewModel.getMenus()
    }

    private fun observeData() {
        viewModel.categories.observe(viewLifecycleOwner) {
            it.proceedWhen(doOnSuccess = {
                binding.layoutStateCategory.root.isVisible = false
                binding.layoutStateCategory.pbLoading.isVisible = false
                binding.layoutStateCategory.tvError.isVisible = false
                binding.rvCategory.apply {
                    isVisible = true
                    adapter = adapterCategory
                    itemAnimator = null
                }
                it.payload?.let { data -> adapterCategory.submitData(data) }
            }, doOnLoading = {
                    binding.layoutStateCategory.root.isVisible = true
                    binding.layoutStateCategory.pbLoading.isVisible = true
                    binding.layoutStateCategory.tvError.isVisible = false
                    binding.rvCategory.isVisible = false
                }, doOnError = {
                    binding.layoutStateCategory.root.isVisible = true
                    binding.layoutStateCategory.pbLoading.isVisible = false
                    binding.layoutStateCategory.tvError.isVisible = true
                    binding.layoutStateCategory.tvError.text = it.exception?.message.orEmpty()
                    binding.rvCategory.isVisible = false
                })
        }

        viewModel.menus.observe(viewLifecycleOwner) {
            it.proceedWhen(doOnSuccess = {
                binding.layoutState.root.isVisible = false
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = false
                binding.rvListMenu.apply {
                    isVisible = true
                    adapter = adapterMenu
                }
                it.payload?.let { data -> adapterMenu.submitData(data) }
            }, doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.rvListMenu.isVisible = false
                }, doOnError = { err ->
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = err.exception?.message.orEmpty()
                    binding.rvListMenu.isVisible = false
                }, doOnEmpty = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = getString(R.string.text_cart_is_empty)
                })
        }
    }

    private fun setupList() {
        val span = if (adapterMenu.adapterLayoutMode == AdapterLayoutMode.LINEAR) 1 else 2
        binding.rvListMenu.apply {
            layoutManager = GridLayoutManager(requireContext(), span)
            adapter = this@HomeFragment.adapterMenu
        }
        adapterMenu.submitData(dataSourceMenu.getMenuList())
    }

    private fun setupSwitch() {
        binding.ivListMode.setOnClickListener() {
            val layoutMode =
                if (adapterMenu.adapterLayoutMode == AdapterLayoutMode.LINEAR) {
                    AdapterLayoutMode.GRID
                } else {
                    AdapterLayoutMode.LINEAR
                }
            val newSpanCount = if (layoutMode == AdapterLayoutMode.GRID) 2 else 1

            adapterMenu.adapterLayoutMode = layoutMode
            (binding.rvListMenu.layoutManager as GridLayoutManager).spanCount = newSpanCount
            val iconMode =
                if (layoutMode == AdapterLayoutMode.GRID) {
                    R.drawable.ic_list
                } else {
                    R.drawable.ic_grid
                }
            binding.ivListMode.setImageResource(iconMode)
        }
    }

    private fun showUserData() {
        val currentUser = viewModel.getCurrentUser()?.fullName
        binding.tvGreeting.text = getString(R.string.text_hi, currentUser)
    }
}
