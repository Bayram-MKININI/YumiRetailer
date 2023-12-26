package net.noliaware.yumi_retailer.commun.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.noliaware.yumi_retailer.commun.ActionTypes.DELETE_CACHED_DEVICE_ID
import net.noliaware.yumi_retailer.commun.ActionTypes.MONITOR
import net.noliaware.yumi_retailer.commun.data.remote.RemoteApi
import net.noliaware.yumi_retailer.commun.domain.model.Action
import net.noliaware.yumi_retailer.commun.domain.model.SessionData
import net.noliaware.yumi_retailer.commun.domain.repository.ActionsRepository
import net.noliaware.yumi_retailer.feature_login.domain.repository.DataStoreRepository
import javax.inject.Inject

class ActionsRepositoryImpl @Inject constructor(
    private val api: RemoteApi,
    private val sessionData: SessionData,
    private val dataStoreRepository: DataStoreRepository
) : ActionsRepository {

    override suspend fun performActions(actions: List<Action>) {
        actions.forEach {
            when (it.type) {
                DELETE_CACHED_DEVICE_ID -> {
                    withContext(Dispatchers.IO) {
                        dataStoreRepository.clearDataStore()
                    }
                }
                MONITOR -> {
                }
            }
        }
    }
}