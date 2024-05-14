package com.example.superagenda.presentation.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.LoginUseCase
import com.example.superagenda.domain.models.UserForLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    fun onUsernameChange(username: String) {
        _username.postValue(username)
    }

    fun onPasswordChange(password: String) {
        _password.postValue(password)
    }

    fun onShow() {
        viewModelScope.launch {
            val isLoggedIn = loginUseCase.isLoggedIn()

            _isLoggedIn.postValue(isLoggedIn)
        }
    }

    fun onLoginButtonPress() {
        viewModelScope.launch {
            val username = _username.value
            val password = _password.value

            if (username.isNullOrBlank() || password.isNullOrBlank()) {
                return@launch
            }

            val userForLogin = UserForLogin(username, password)
            val userAuthenticated = loginUseCase.login(userForLogin)

            _isLoggedIn.postValue(userAuthenticated)
        }
    }
}