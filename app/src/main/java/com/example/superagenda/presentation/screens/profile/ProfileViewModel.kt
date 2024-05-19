package com.example.superagenda.presentation.screens.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.GetUserProfileUseCase
import com.example.superagenda.domain.models.UserForProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {
    private val _userForProfile = MutableLiveData<UserForProfile?>()
    val userForProfile: LiveData<UserForProfile?> = _userForProfile

    fun onShow() {
        viewModelScope.launch {
            val userProfile = getUserProfileUseCase.retrieveUserForProfile()

            _userForProfile.postValue(userProfile)
        }
    }
}