package com.ackerman.foodappme.presentation.feature.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.ackerman.foodappme.R
import com.ackerman.foodappme.databinding.FragmentProfileBinding
import com.ackerman.foodappme.presentation.feature.editprofile.EditProfileActivity
import com.ackerman.foodappme.presentation.feature.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showUserData()
        setupForm()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnChangeProfile.setOnClickListener {
            navigateToEditProfile()
        }
        binding.llLogout.setOnClickListener {
            doLogout()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun doLogout() {
        AlertDialog.Builder(requireContext())
            .setMessage(
                getString(R.string.text_do_you_want_to_logout)
            )
            .setPositiveButton("Yes") { _, _ ->
                viewModel.doLogout()
                navigateToLogin()
            }.setNegativeButton("No") { _, _ ->
                // do nothing
            }.create().show()
    }

    private fun navigateToEditProfile() {
        val intent = Intent(requireContext(), EditProfileActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
    }

    private fun showUserData() {
        binding.layoutForm.etName.setText(viewModel.getCurrentUser()?.fullName)
        binding.layoutForm.etEmail.setText(viewModel.getCurrentUser()?.email)
        binding.layoutForm.tilEmail.isEnabled = false
    }

    private fun setupForm() {
        binding.layoutForm.tilName.isVisible = true
        binding.layoutForm.tilEmail.isVisible = true
        binding.clProfile.isVisible = true
    }
}
