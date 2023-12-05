package net.noliaware.yumi_retailer.feature_profile.presentation.mappers

import android.content.Context
import net.noliaware.yumi_retailer.feature_profile.domain.model.Voucher
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VoucherItemView.VoucherItemViewAdapter

interface VoucherMapper {
    fun mapVoucher(
        context: Context,
        color: Int,
        voucher: Voucher
    ): VoucherItemViewAdapter
}