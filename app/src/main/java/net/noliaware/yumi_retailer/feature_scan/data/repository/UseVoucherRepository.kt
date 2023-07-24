package net.noliaware.yumi_retailer.feature_scan.data.repository

import kotlinx.coroutines.flow.Flow
import net.noliaware.yumi_retailer.commun.util.Resource

interface UseVoucherRepository {
    fun updatePrivacyPolicyReadStatus(): Flow<Resource<Boolean>>
    fun useVoucherByCode(voucherCode: String): Flow<Resource<Boolean>>
}