package com.ackerman.foodappme.presentation.feature.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ackerman.foodappme.data.repository.UserRepository
import com.ackerman.foodappme.utils.ResultWrapper
import kotlinx.coroutines.launch

class EditProfileViewModel(private val repo: UserRepository) : ViewModel() {
    private val _dataProfile = MutableLiveData<ResultWrapper<Boolean>>()

    val dataProfile: LiveData<ResultWrapper<Boolean>>
        get() = _dataProfile

    fun getCurrentUser() = repo.getCurrentUser()

    fun createChangePwdRequest() {
        repo.sendChangePasswordRequestByEmail()
    }
    fun updateProfile(fullName:String){
        viewModelScope.launch {
            repo.updateProfile(fullName).collect{
                _dataProfile.postValue(it)
            }
        }
    }

}