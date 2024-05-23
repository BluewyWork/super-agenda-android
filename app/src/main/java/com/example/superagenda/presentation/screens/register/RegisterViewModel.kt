package com.example.superagenda.presentation.screens.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.core.navigations.Destinations
import com.example.superagenda.domain.LoginUseCase
import com.example.superagenda.domain.RegisterUseCase
import com.example.superagenda.domain.models.UserForLogin
import com.example.superagenda.domain.models.UserForRegister
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    fun onRegisterButtonPress(navController: NavController) {
        viewModelScope.launch {
            val userForRegister = _username.value?.let {
                _password.value?.let { it1 ->
                    UserForRegister(
                        username = it,
                        password = it1
                    )
                }
            }

            if (userForRegister != null) {
                val ok = registerUseCase.register(userForRegister)

                if (!ok) {
                    // do something here
                    return@launch
                }

                val userForLogin = UserForLogin(
                    username = userForRegister.username,
                    password = userForRegister.password
                )

                val ok2 = loginUseCase.login(userForLogin)

                if (!ok2) {
                    // do something here
                    return@launch
                }

                navController.navigate(Destinations.TasksNotStarted.route)
            }
        }
    }

    fun onUsernameChange(username: String) {
        _username.postValue(username)
    }

    fun onPasswordChange(password: String) {
        _password.postValue(password)
    }
}