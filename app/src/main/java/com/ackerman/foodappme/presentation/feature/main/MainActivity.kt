package com.ackerman.foodappme.presentation.feature.main

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ackerman.foodappme.R
import com.ackerman.foodappme.data.local.datastore.UserPreferenceDataSourceImpl
import com.ackerman.foodappme.data.local.datastore.appDataStore
import com.ackerman.foodappme.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.ackerman.foodappme.data.repository.UserRepositoryImpl
import com.ackerman.foodappme.databinding.ActivityMainBinding
import com.ackerman.foodappme.utils.GenericViewModelFactory
import com.ackerman.foodappme.utils.PreferenceDataStoreHelperImpl
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels {
        GenericViewModelFactory.create(createViewModel())
    }

    private fun createViewModel(): MainViewModel {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo = UserRepositoryImpl(dataSource)
        return MainViewModel(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupBottomNav()
    }

    private fun setupBottomNav() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(navController)
    }


}