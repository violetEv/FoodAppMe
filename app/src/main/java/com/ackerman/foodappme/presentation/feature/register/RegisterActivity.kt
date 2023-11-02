package com.ackerman.foodappme.presentation.feature.register

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.ackerman.foodappme.R
import com.ackerman.foodappme.databinding.ActivityRegisterBinding
import com.ackerman.foodappme.presentation.feature.login.LoginActivity
import com.ackerman.foodappme.presentation.feature.main.MainActivity
import com.ackerman.foodappme.utils.highLightWord
import com.ackerman.foodappme.utils.proceedWhen
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupForm()
        setClickListeners()
        observeResult()
    }

    private fun setupForm() {
        binding.layoutForm.tilName.isVisible = true
        binding.layoutForm.tilEmail.isVisible = true
        binding.layoutForm.tilPassword.isVisible = true
        binding.layoutForm.tilConfirmPassword.isVisible = true
    }

    private fun setClickListeners() {
        binding.tvNavToLogin.highLightWord(getString(R.string.text_highlight_login_here)) {
            navigateToLogin()
        }
        binding.btnRegister.setOnClickListener {
            doRegister()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
    }

    private fun doRegister() {
        if (isFormValid()) {
            val name = binding.layoutForm.etName.text.toString().trim()
            val email = binding.layoutForm.etEmail.text.toString().trim()
            val password = binding.layoutForm.etPassword.text.toString().trim()
            viewModel.doRegister(name, email, password)
        }
    }

    private fun observeResult() {
        viewModel.registerResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnRegister.isVisible = true
                    binding.btnRegister.isEnabled = false
                    navigateToMain()
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnRegister.isVisible = false
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnRegister.isVisible = true
                    binding.btnRegister.isEnabled = true
                    Toast.makeText(
                        this,
                        "Register Failed: ${it.exception?.message.orEmpty()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
    }

    private fun isFormValid(): Boolean {
        // ambil nilai dari form yg sudah kita punya
        // trim biar tidak ada spasi kanan dan kiri
        val name = binding.layoutForm.etName.text.toString().trim()
        val email = binding.layoutForm.etEmail.text.toString().trim()
        val password = binding.layoutForm.etPassword.text.toString().trim()
        val confirmPassword = binding.layoutForm.etConfirmPassword.text.toString().trim()
        return checkNameValidation(name) && checkEmailValidation(email) &&
            checkPasswordValidation(password, binding.layoutForm.tilPassword) &&
            checkPasswordValidation(confirmPassword, binding.layoutForm.tilConfirmPassword) &&
            checkPwdAndConfirmPwd(password, confirmPassword)
    }

    private fun checkNameValidation(fullName: String): Boolean {
        return if (fullName.isEmpty()) {
            binding.layoutForm.tilName.isErrorEnabled = true
            binding.layoutForm.tilName.error = getString(R.string.text_error_name_cannot_empty)
            false
        } else {
            binding.layoutForm.tilName.isErrorEnabled = false
            true
        }
    }

    private fun checkEmailValidation(email: String): Boolean {
        return if (email.isEmpty()) {
            binding.layoutForm.tilEmail.isErrorEnabled = true
            binding.layoutForm.tilEmail.error = getString(R.string.text_error_email_empty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.layoutForm.tilEmail.isErrorEnabled = true
            binding.layoutForm.tilEmail.error = getString(R.string.text_error_email_invalid)
            false
        } else {
            binding.layoutForm.tilEmail.isErrorEnabled = false
            true
        }
    }

    private fun checkPasswordValidation(
        password: String,
        textInputLayout: TextInputLayout
    ): Boolean {
        return if (password.isEmpty()) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = getString(R.string.text_error_password_empty)
            false
        } else if (password.length < 8) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = getString(R.string.text_password_cannot_less_than_8_character)
            false
        } else {
            textInputLayout.isErrorEnabled = false
            true
        }
    }

    private fun checkPwdAndConfirmPwd(password: String, confirmPassword: String): Boolean {
        return if (password != confirmPassword) {
            binding.layoutForm.tilEmail.isErrorEnabled = true
            binding.layoutForm.tilConfirmPassword.isErrorEnabled = true
            binding.layoutForm.tilPassword.error = getString(R.string.text_password_doesn_t_match)
            binding.layoutForm.tilConfirmPassword.error =
                getString(R.string.text_password_doesn_t_match)
            false
        } else {
            binding.layoutForm.tilPassword.isErrorEnabled = false
            binding.layoutForm.tilPassword.isErrorEnabled = false
            true
        }
    }
}
