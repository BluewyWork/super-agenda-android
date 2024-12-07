package com.example.superagenda.presentation.screens.slider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superagenda.domain.MiscUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SliderScreenViewModel @Inject constructor(
   private val miscUseCase: MiscUseCase
) : ViewModel() {
   fun saveShownTrue() {
      viewModelScope.launch {
         miscUseCase.updateScreenShownAtDatabase(true)
      }
   }
}