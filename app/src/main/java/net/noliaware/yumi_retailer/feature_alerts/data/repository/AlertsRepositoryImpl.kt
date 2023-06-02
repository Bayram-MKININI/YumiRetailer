package net.noliaware.yumi_retailer.feature_alerts.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import net.noliaware.yumi_retailer.commun.LIST_PAGE_SIZE
import net.noliaware.yumi_retailer.commun.data.remote.RemoteApi
import net.noliaware.yumi_retailer.commun.domain.model.SessionData

class AlertsRepositoryImpl(
    private val api: RemoteApi,
    private val sessionData: SessionData
) : AlertsRepository {

    override fun getAlertList() = Pager(
        PagingConfig(
            pageSize = LIST_PAGE_SIZE,
            enablePlaceholders = false
        )
    ) {
        AlertPagingSource(api, sessionData)
    }.flow
}