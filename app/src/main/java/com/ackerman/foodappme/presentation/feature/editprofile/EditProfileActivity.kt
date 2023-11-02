package com.ackerman.foodappme.presentation.feature.editprofile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ackerman.foodappme.R
import com.ackerman.foodappme.databinding.ActivityEditProfileBinding
import com.ackerman.foodappme.presentation.feature.profile.ProfileFragment
import com.ackerman.foodappme.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileActivity : AppCompatActivity() {
    private val binding: ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }
    private val viewModel: EditProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupForm()
        showData()
        setClickListeners()
        observeData()
    }

    private fun showData() {
        binding.layoutForm.etName.setText(viewModel.getCurrentUser()?.fullName)
        binding.layoutForm.etEmail.setText(viewModel.getCurrentUser()?.email)
        binding.layoutForm.tilEmail.isEnabled = false
    }

    private fun observeData() {
        viewModel.dataProfile.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.btnSaveChangeProfile.isVisible = true
                    binding.btnSaveChangeProfile.isEnabled = true
                    binding.pbLoading.isVisible = false
                    navigateToProfile()
                    Toast.makeText(this, "Edit profile success!", Toast.LENGTH_SHORT).show()
                },
                doOnLoading = {
                    binding.btnSaveChangeProfile.isVisible = false
                    binding.pbLoading.isVisible = true
                }
            )
        }
    }

    private fun setClickListeners() {
        binding.tvChangePwd.setOnClickListener {
            requestChangePassword()
        }
        binding.btnSaveChangeProfile.setOnClickListener {
            changeProfileData()
        }
    }

    private fun isFormValid(): Boolean {
        val newName = binding.layoutForm.etName.text.toString().trim()
        return checkNameValidation(newName)
    }

    private fun checkNameValidation(newName: String): Boolean {
        return if (newName.isEmpty()) {
            binding.layoutForm.tilName.isErrorEnabled = true
            binding.layoutForm.tilName.error = getString(R.string.text_error_name_cannot_empty)
            false
        } else {
            binding.layoutForm.tilName.isErrorEnabled = false
            true
        }
    }

    private fun changeProfileData() {
        if (isFormValid()) {
            val fullName = binding.layoutForm.etName.text.toString().trim()
            viewModel.updateProfile(fullName)
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
                getString(R.string.text_change_password_request_sent_to_your_email) +
                    "\$(viewModel.getCurrentUser()?.email)\""
            )
            .setPositiveButton("Okay") { _, _ ->
            }.create().show()
    }

    private fun setupForm() {
        binding.layoutForm.tilName.isVisible = true
        binding.layoutForm.tilEmail.isVisible = true
    }
}
