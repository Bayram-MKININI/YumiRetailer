package net.noliaware.yumi_retailer.feature_profile.presentation.mappers

import android.content.Context
import android.text.SpannableString
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.DateTime.SHORT_DATE_FORMAT
import net.noliaware.yumi_retailer.commun.util.DecoratedText
import net.noliaware.yumi_retailer.commun.util.decorateWords
import net.noliaware.yumi_retailer.commun.util.getFontFromResources
import net.noliaware.yumi_retailer.commun.util.parseDateToFormat
import net.noliaware.yumi_retailer.feature_profile.domain.model.Voucher
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherDeliveryStatus.AVAILABLE
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherDeliveryStatus.NON_AVAILABLE
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherDeliveryStatus.ON_HOLD
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VoucherItemView.VoucherItemViewAdapter
import javax.inject.Inject

class AvailableVoucherMapper @Inject constructor() : VoucherMapper {

    override fun mapVoucher(
        context: Context,
        color: Int,
        voucher: Voucher
    ) = VoucherItemViewAdapter(
        isDeactivated = voucher.voucherDeliveryStatus != AVAILABLE,
        color = color,
        hasOngoingRequests = mapHasOngoingRequests(voucher),
        title = voucher.productLabel.orEmpty(),
        highlight = mapHighlight(context, voucher)
    )

    private fun mapHasOngoingRequests(
        voucher: Voucher
    ) = voucher.voucherDeliveryStatus != ON_HOLD && voucher.voucherOngoingRequestCount > 0

    private fun mapHighlight(
        context: Context,
        voucher: Voucher
    ) = when (voucher.voucherDeliveryStatus) {
        ON_HOLD -> SpannableString(context.getString(R.string.on_hold))
        NON_AVAILABLE -> {
            val startDate = voucher.voucherStartDate?.parseDateToFormat(SHORT_DATE_FORMAT).orEmpty()
            context.getString(
                getSingleDateResIdOrDefault(voucher, R.string.usable_start_date_value),
                startDate
            ).decorateWords(
                wordsToDecorate = listOf(
                    decorateTextWithFont(context, startDate)
                )
            )
        }
        else -> {
            val expiryDate = voucher.voucherExpiryDate?.parseDateToFormat(SHORT_DATE_FORMAT).orEmpty()
            context.getString(
                getSingleDateResIdOrDefault(voucher, R.string.usable_end_date_value),
                expiryDate
            ).decorateWords(
                wordsToDecorate = listOf(
                    decorateTextWithFont(context, expiryDate)
                )
            )
        }
    }

    private fun getSingleDateResIdOrDefault(
        voucher: Voucher,
        defaultResId: Int
    ) = if (voucher.voucherStartDate == voucher.voucherExpiryDate) {
        R.string.usable_single_date_value
    } else {
        defaultResId
    }

    private fun decorateTextWithFont(
        context: Context,
        startDate: String
    ) = DecoratedText(
        textToDecorate = startDate,
        typeface = context.getFontFromResources(R.font.omnes_semibold_regular)
    )
}