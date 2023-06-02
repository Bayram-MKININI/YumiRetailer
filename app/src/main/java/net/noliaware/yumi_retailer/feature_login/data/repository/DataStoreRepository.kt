package net.noliaware.yumi_retailer.feature_login.data.repository

import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi_retailer.feature_login.domain.model.UserPreferences

interface DataStoreRepository {

    suspend fun saveLogin(login: String)

    suspend fun saveDeviceId(deviceId: String)

    fun readUserPreferences(): Flow<UserPreferences>

    suspend fun clearDataStore()
}