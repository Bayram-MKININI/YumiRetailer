package net.noliaware.yumi_retailer.feature_alerts.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi_retailer.feature_alerts.domain.model.Alert

interface AlertsRepository {
    fun getAlertList(): Flow<PagingData<Alert>>
}