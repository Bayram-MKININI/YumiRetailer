package net.noliaware.yumi_retailer.feature_login.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class LoginActivityViewModel @Inject constructor(): ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(Duration.ofMillis(400))
            _isLoading.value = false
        }
    }
}