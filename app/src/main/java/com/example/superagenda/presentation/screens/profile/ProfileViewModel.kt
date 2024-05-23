package com.example.superagenda.presentation.screens.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.superagenda.core.navigations.Destinations
import com.example.superagenda.domain.ProfileUseCase
import com.example.superagenda.domain.models.UserForProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase
) : ViewModel() {
    private val _userForProfile = MutableLiveData<UserForProfile?>()
    val userForProfile: LiveData<UserForProfile?> = _userForProfile

    fun onShow() {
        viewModelScope.launch {
            val userProfile = profileUseCase.retrieveUserForProfile()

            _userForProfile.postValue(userProfile)
        }
    }

    fun onDeleteButtonPressButton(navController: NavController) {
        viewModelScope.launch {
            if (!profileUseCase.deleteProfile()) {
                return@launch
            }

            navController.navigate(Destinations.Login.route)
        }
    }
}