package net.noliaware.yumi_retailer.feature_profile.presentation.mappers

import android.content.Context
import android.text.SpannableString
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.DateTime.HOURS_TIME_FORMAT
import net.noliaware.yumi_retailer.commun.DateTime.SHORT_DATE_FORMAT
import net.noliaware.yumi_retailer.commun.util.DecoratedText
import net.noliaware.yumi_retailer.commun.util.decorateWords
import net.noliaware.yumi_retailer.commun.util.getFontFromResources
import net.noliaware.yumi_retailer.commun.util.parseDateToFormat
import net.noliaware.yumi_retailer.commun.util.parseTimeToFormat
import net.noliaware.yumi_retailer.feature_profile.domain.model.Voucher
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VoucherItemView.VoucherItemViewAdapter
import javax.inject.Inject

class UsedVoucherMapper @Inject constructor() : VoucherMapper {

    override fun mapVoucher(
        context: Context,
        color: Int,
        voucher: Voucher
    ) = VoucherItemViewAdapter(
        color = color,
        highlight = mapHighlight(context, voucher),
        title = voucher.productLabel.orEmpty()
    )

    private fun mapHighlight(
        context: Context,
        voucher: Voucher
    ): SpannableString {
        val useDate = voucher.voucherUseDate?.parseDateToFormat(SHORT_DATE_FORMAT)
        val useTime = voucher.voucherUseTime?.parseTimeToFormat(HOURS_TIME_FORMAT)
        return context.getString(
            R.string.usage_date,
            context.getString(R.string.date_time, useDate, useTime)
        ).decorateWords(
            wordsToDecorate = listOf(
                DecoratedText(
                    textToDecorate = useDate.orEmpty(),
                    typeface = context.getFontFromResources(R.font.omnes_semibold_regular)
                ),
                DecoratedText(
                    textToDecorate = useTime.orEmpty(),
                    typeface = context.getFontFromResources(R.font.omnes_semibold_regular)
                )
            )
        )
    }
}
