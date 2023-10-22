package com.ackerman.foodappme.presentation.feature.editprofile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ackerman.foodappme.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.ackerman.foodappme.data.repository.UserRepositoryImpl
import com.ackerman.foodappme.databinding.ActivityEditProfileBinding
import com.ackerman.foodappme.presentation.feature.profile.ProfileFragment
import com.ackerman.foodappme.utils.GenericViewModelFactory
import com.ackerman.foodappme.utils.proceedWhen
import com.google.firebase.auth.FirebaseAuth

class EditProfileActivity : AppCompatActivity() {
    private val binding: ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }
    private val viewModel:EditProfileViewModel by viewModels{
            GenericViewModelFactory.create(createViewModel()) }

    private fun createViewModel(): EditProfileViewModel {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo = UserRepositoryImpl(dataSource)
        return EditProfileViewModel(repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupForm()
        showData()
        setClickListeners()
        observeData()
    }

    private fun showData() {
        val user = viewModel.getCurrentUser()
        if (user != null) {
            binding.tfUsername.setText(user.fullName)
            binding.tfEmail.setText(user.email)
        } else {
            binding.tfUsername.setText("User not found")
        }
    }


    private fun observeData() {
        viewModel.dataProfile.observe(this){
            it.proceedWhen(
                doOnSuccess = {
                    binding.clEditLayout.isVisible= true
                    binding.pbLoading.isVisible = false
                },
                doOnLoading = {
                    binding.clEditLayout.isVisible = false
                    binding.pbLoading.isVisible = true
                }
            )
        }
    }

    private fun setClickListeners() {
        binding.ivEditPassword.setOnClickListener{
            requestChangePassword()
        }
        binding.ivCheck.setOnClickListener{
            navigateToProfile()
        }
    }

    private fun navigateToProfile() {
        val intent = Intent(this, ProfileFragment::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun requestChangePassword() {
        viewModel.createChangePwdRequest()
        AlertDialog.Builder(this)
            .setMessage(
                "Change Password request sent to your email"+
                        "\$(viewModel.getCurrentUser()?.email)\""
            )
            .setPositiveButton("Okay"){_,_->

            }.create().show()
    }


    private fun setupForm() {
        binding.tfUsername.isVisible = true
        binding.tfEmail.isVisible = true
        binding.tfPassword.isVisible = true
    }
}