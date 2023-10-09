package com.ackerman.foodappme.presentation.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ackerman.foodappme.R
import com.ackerman.foodappme.data.dummy.DummyCategoryDataSource
import com.ackerman.foodappme.data.dummy.DummyCategoryDataSourceImpl
import com.ackerman.foodappme.data.dummy.DummyMenuDataSource
import com.ackerman.foodappme.data.dummy.DummyMenuDataSourceImpl
import com.ackerman.foodappme.data.local.database.AppDatabase
import com.ackerman.foodappme.data.local.database.datasource.MenuDataSource
import com.ackerman.foodappme.data.local.database.datasource.MenuDatabaseDataSource
import com.ackerman.foodappme.data.repository.MenuRepository
import com.ackerman.foodappme.data.repository.MenuRepositoryImpl
import com.ackerman.foodappme.databinding.FragmentHomeBinding
import com.ackerman.foodappme.model.Menu
import com.ackerman.foodappme.presentation.feature.detail.DetailActivity
import com.ackerman.foodappme.presentation.feature.home.adapter.AdapterLayoutMode
import com.ackerman.foodappme.presentation.feature.home.adapter.CategoryListAdapter
import com.ackerman.foodappme.presentation.feature.home.adapter.MenuListAdapter
import com.ackerman.foodappme.utils.GenericViewModelFactory
import com.ackerman.foodappme.utils.proceedWhen

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val dataSourceMenu: DummyMenuDataSource by lazy {
        DummyMenuDataSourceImpl()
    }
    private val dataSourceCategory: DummyCategoryDataSourceImpl by lazy {
        DummyCategoryDataSourceImpl()
    }
    private val adapter: MenuListAdapter by lazy {
        MenuListAdapter(AdapterLayoutMode.LINEAR) {
            navigateToDetail(it)
        }
    }
    private val adapterCategory: CategoryListAdapter by lazy {
        CategoryListAdapter {
            Toast.makeText(binding.root.context, it.name, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToDetail(menu: Menu) {
        DetailActivity.startActivity(requireContext(), menu)
    }

    private val viewModel: HomeViewModel by viewModels() {
        val cds: DummyCategoryDataSource = DummyCategoryDataSourceImpl()
        val database = AppDatabase.getInstance(requireContext())
        val menuDao = database.menuDao()
        val menuDataSource: MenuDataSource = MenuDatabaseDataSource(menuDao)
        val repo: MenuRepository = MenuRepositoryImpl(menuDataSource, cds)
        GenericViewModelFactory.create(HomeViewModel(repo))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        showCategory()
        setupSwitch()
        observeData()
    }

    private fun observeData() {
        viewModel.menuList.observe(viewLifecycleOwner) {
            it.proceedWhen(doOnSuccess = { result ->
                binding.layoutState.root.isVisible = false
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = false
                binding.rvListMenu.apply {
                    isVisible = true
                    adapter = this@HomeFragment.adapter
                }
                it.payload?.let { data-> adapter.submitData(data) }
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
            }, doOnEmpty = { data ->
                binding.layoutState.root.isVisible = true
                binding.layoutState.pbLoading.isVisible = false
                binding.layoutState.tvError.isVisible = true
                binding.layoutState.tvError.text = getString(R.string.text_cart_is_empty)
            }
            )
        }
    }

    private fun showCategory() {
        binding.rvCategory.adapter = adapterCategory
        binding.rvCategory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapterCategory.setData(dataSourceCategory.getMenuCategory())
    }


    private fun setupList() {
        val span = if (adapter.adapterLayoutMode == AdapterLayoutMode.LINEAR) 1 else 2
        binding.rvListMenu.apply {
            layoutManager = GridLayoutManager(requireContext(), span)
            adapter = this@HomeFragment.adapter
        }
        adapter.submitData(dataSourceMenu.getMenuList())
    }

    private fun setupSwitch() {
        binding.ivListMode.setOnClickListener() {
            val layoutMode =
                if (adapter.adapterLayoutMode == AdapterLayoutMode.LINEAR) AdapterLayoutMode.GRID
                else AdapterLayoutMode.LINEAR
            val newSpanCount = if (layoutMode == AdapterLayoutMode.GRID) 2 else 1

            adapter.adapterLayoutMode = layoutMode
            (binding.rvListMenu.layoutManager as GridLayoutManager).spanCount = newSpanCount

            val iconMode =
                if (layoutMode == AdapterLayoutMode.GRID) R.drawable.ic_list
                else R.drawable.ic_grid
            binding.ivListMode.setImageResource(iconMode)
        }
    }
}

