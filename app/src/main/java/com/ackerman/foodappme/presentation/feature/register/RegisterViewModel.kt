package com.ackerman.foodappme.presentation.feature.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ackerman.foodappme.data.repository.UserRepository
import com.ackerman.foodappme.utils.ResultWrapper
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<ResultWrapper<Boolean>>()
    val registerResult : LiveData<ResultWrapper<Boolean>>
        get() = _registerResult

    fun doRegister(fullName: String, email: String, password:String){
        viewModelScope.launch {
            repository.doRegister(fullName,email,password).collect{result->
                _registerResult.postValue(result)
            }
        }
    }

}