package com.example.superagenda.presentation.composables

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
   private val loginUseCase: LoginUseCase
) : ViewModel() {
   private val _isLoggedIn = MutableLiveData<Boolean>()
   val isLoggedIn: LiveData<Boolean> = _isLoggedIn

   fun onShow() {
      viewModelScope.launch {
         _isLoggedIn.postValue(loginUseCase.isLoggedIn())
      }
   }
}