package com.ackerman.foodappme.presentation.feature.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ackerman.foodappme.data.network.firebase.auth.FirebaseAuthDataSource
import com.ackerman.foodappme.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.ackerman.foodappme.data.repository.UserRepository
import com.ackerman.foodappme.data.repository.UserRepositoryImpl
import com.ackerman.foodappme.databinding.ActivitySplashScreenBinding
import com.ackerman.foodappme.presentation.feature.login.LoginActivity
import com.ackerman.foodappme.presentation.feature.main.MainActivity
import com.ackerman.foodappme.utils.GenericViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    private val binding: ActivitySplashScreenBinding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }
    private val viewModel: SplashScreenViewModel by viewModels {
        GenericViewModelFactory.create(createViewModel())
    }

    private fun createViewModel(): SplashScreenViewModel {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource: FirebaseAuthDataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repository: UserRepository = UserRepositoryImpl(dataSource)
        return SplashScreenViewModel(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkIfUserLogin()
    }

    private fun checkIfUserLogin() {
        lifecycleScope.launch {
            delay(2000)
            if (viewModel.isUserLoggedIn()) {
                navigateToLogin()
            } else {
                navigateToMain()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun navigateToLogin(){
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }
}
