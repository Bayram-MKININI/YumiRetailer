package net.noliaware.yumi_retailer.feature_alerts.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi_retailer.feature_alerts.data.repository.AlertsRepository
import javax.inject.Inject

@HiltViewModel
class AlertsFragmentViewModel @Inject constructor(
    private val repository: AlertsRepository
) : ViewModel() {
    fun getAlerts() = repository.getAlertList().cachedIn(viewModelScope)
}