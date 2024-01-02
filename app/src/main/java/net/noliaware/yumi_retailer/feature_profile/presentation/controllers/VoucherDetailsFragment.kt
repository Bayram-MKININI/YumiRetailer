package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import android.content.DialogInterface
import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.DateTime.HOURS_TIME_FORMAT
import net.noliaware.yumi_retailer.commun.DateTime.SHORT_DATE_FORMAT
import net.noliaware.yumi_retailer.commun.FragmentKeys.REFRESH_VOUCHER_DETAILS_REQUEST_KEY
import net.noliaware.yumi_retailer.commun.FragmentKeys.REFRESH_VOUCHER_LIST_REQUEST_KEY
import net.noliaware.yumi_retailer.commun.util.DecoratedText
import net.noliaware.yumi_retailer.commun.util.ViewState.DataState
import net.noliaware.yumi_retailer.commun.util.ViewState.LoadingState
import net.noliaware.yumi_retailer.commun.util.collectLifecycleAware
import net.noliaware.yumi_retailer.commun.util.decorateWords
import net.noliaware.yumi_retailer.commun.util.getFontFromResources
import net.noliaware.yumi_retailer.commun.util.handleSharedEvent
import net.noliaware.yumi_retailer.commun.util.navDismiss
import net.noliaware.yumi_retailer.commun.util.openWebPage
import net.noliaware.yumi_retailer.commun.util.parseDateToFormat
import net.noliaware.yumi_retailer.commun.util.parseTimeToFormat
import net.noliaware.yumi_retailer.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi_retailer.commun.util.safeNavigate
import net.noliaware.yumi_retailer.feature_login.domain.model.VoucherRequestType
import net.noliaware.yumi_retailer.feature_profile.domain.model.Voucher
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherDeliveryStatus.AVAILABLE
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherDeliveryStatus.NON_AVAILABLE
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherDeliveryStatus.ON_HOLD
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherStatus.CANCELLED
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherStatus.INEXISTENT
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherStatus.USABLE
import net.noliaware.yumi_retailer.feature_profile.domain.model.VoucherStatus.USED
import net.noliaware.yumi_retailer.feature_profile.presentation.adapters.VoucherRequestsAdapter
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VoucherRequestView
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VoucherRequestView.VoucherRequestViewAdapter
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VouchersDetailsContainerView
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VouchersDetailsContainerView.VouchersDetailsViewAdapter
import net.noliaware.yumi_retailer.feature_profile.presentation.views.VouchersDetailsContainerView.VouchersDetailsViewCallback

@AndroidEntryPoint
class VoucherDetailsFragment : AppCompatDialogFragment() {

    private var vouchersDetailsContainerView: VouchersDetailsContainerView? = null
    private val args by navArgs<VoucherDetailsFragmentArgs>()
    private val viewModel by viewModels<VoucherDetailsFragmentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.vouchers_details_layout,
        container,
        false
    ).apply {
        vouchersDetailsContainerView = this as VouchersDetailsContainerView
        vouchersDetailsContainerView?.callback = vouchersDetailsViewCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFragmentListener()
        collectFlows()
        setUpRequestsDropdownView()
        vouchersDetailsContainerView?.activateLoading(true)
        vouchersDetailsContainerView?.setUpViewLook(
            color = args.categoryUI.categoryColor,
            iconName = args.categoryUI.categoryIcon
        )
    }

    private fun setUpFragmentListener() {
        setFragmentResultListener(
            REFRESH_VOUCHER_DETAILS_REQUEST_KEY
        ) { _, _ ->
            viewModel.callGetVoucher()
            viewModel.voucherListShouldRefresh = true
        }
    }

    private fun collectFlows() {
        viewModel.getVoucherEventsHelper.eventFlow.collectLifecycleAware(viewLifecycleOwner) { sharedEvent ->
            vouchersDetailsContainerView?.activateLoading(false)
            handleSharedEvent(sharedEvent)
            redirectToLoginScreenFromSharedEvent(sharedEvent)
        }
        viewModel.getVoucherEventsHelper.stateFlow.collectLifecycleAware(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is LoadingState -> Unit
                is DataState -> viewState.data?.let { voucher ->
                    vouchersDetailsContainerView?.activateLoading(false)
                    bindViewToData(voucher)
                }
            }
        }

        viewModel.requestSentEventsHelper.eventFlow.collectLifecycleAware(viewLifecycleOwner) { sharedEvent ->
            handleSharedEvent(sharedEvent)
            redirectToLoginScreenFromSharedEvent(sharedEvent)
        }
        viewModel.requestSentEventsHelper.stateFlow.collectLifecycleAware(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is LoadingState -> Unit
                is DataState -> viewState.data?.let { requestSent ->
                    if (requestSent) {
                        viewModel.callGetVoucher()
                        viewModel.voucherListShouldRefresh = true
                    }
                }
            }
        }

        viewModel.setVoucherAvailabilityEventsHelper.eventFlow.collectLifecycleAware(viewLifecycleOwner) { sharedEvent ->
            handleSharedEvent(sharedEvent)
            redirectToLoginScreenFromSharedEvent(sharedEvent)
        }
        viewModel.setVoucherAvailabilityEventsHelper.stateFlow.collectLifecycleAware(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is LoadingState -> Unit
                is DataState -> viewState.data?.let { requestSent ->
                    if (requestSent) {
                        viewModel.callGetVoucher()
                        viewModel.voucherListShouldRefresh = true
                    }
                }
            }
        }
    }

    private fun bindViewToData(voucher: Voucher) {
        vouchersDetailsContainerView?.fillViewWithData(
            VouchersDetailsViewAdapter(
                title = voucher.productLabel.orEmpty(),
                titleCrossed = voucher.voucherStatus != USABLE,
                voucherDate = mapVoucherDate(voucher),
                requestsAvailable = mapVoucherRequestsAvailable(voucher),
                voucherNumber = mapVoucherNumber(voucher.voucherNumber),
                ongoingRequestsAvailable = voucher.voucherOngoingRequestCount > 0,
                voucherDescription = voucher.productDescription,
                voucherPrice = mapVoucherPrice(voucher),
                moreActionAvailable = voucher.productWebpage?.isNotEmpty() == true,
                amendDatesAvailable = voucher.voucherStatus == USABLE,
                startDate = mapStartDate(voucher),
                endDate = mapEndDate(voucher),
                voucherStatus = mapVoucherStatus(voucher)
            )
        )
    }

    private fun mapVoucherRequestsAvailable(
        voucher: Voucher
    ) = voucher.voucherStatus == USABLE && args.requestTypes?.isNotEmpty() == true

    private fun mapVoucherNumber(
        voucherNumber: String
    ) = decorateTextWithFont(
        getString(R.string.voucher_number, voucherNumber),
        listOf(voucherNumber)
    )

    private fun mapVoucherDate(
        voucher: Voucher
    ) = when (voucher.voucherStatus) {
        USABLE -> {
            val creationDate = voucher.voucherDate?.parseDateToFormat(SHORT_DATE_FORMAT).orEmpty()
            decorateTextWithFont(
                originalText = getString(R.string.created_in, creationDate),
                wordsToStyle = listOf(creationDate)
            )
        }
        USED -> {
            val usageDate = voucher.voucherUseDate?.parseDateToFormat(SHORT_DATE_FORMAT).orEmpty()
            val usageTime = voucher.voucherUseTime?.parseTimeToFormat(HOURS_TIME_FORMAT).orEmpty()
            decorateTextWithFont(
                originalText = getString(
                    R.string.usage_date,
                    getString(R.string.date_time, usageDate, usageTime)
                ),
                wordsToStyle = listOf(usageDate, usageTime)
            )
        }
        CANCELLED -> {
            val cancellationDate = voucher.voucherUseDate?.parseDateToFormat(SHORT_DATE_FORMAT).orEmpty()
            val cancellationTime = voucher.voucherUseTime?.parseTimeToFormat(HOURS_TIME_FORMAT).orEmpty()
            decorateTextWithFont(
                originalText = getString(
                    R.string.cancellation_date,
                    getString(R.string.date_time, cancellationDate, cancellationTime)
                ),
                wordsToStyle = listOf(cancellationDate, cancellationTime)
            )
        }
        else -> SpannableString("")
    }

    private fun mapStartDate(voucher: Voucher): SpannableString {
        val startDate = voucher.voucherStartDate?.parseDateToFormat(SHORT_DATE_FORMAT).orEmpty()
        val textResId = if (voucher.voucherStartDate == voucher.voucherExpiryDate) {
            R.string.usable_single_date_value
        } else {
            R.string.usable_start_date_value
        }
        return decorateTextWithFont(
            originalText = getString(textResId, startDate),
            wordsToStyle = listOf(startDate)
        )
    }

    private fun mapEndDate(voucher: Voucher): SpannableString? {
        if (voucher.voucherStartDate == voucher.voucherExpiryDate) {
            return null
        }
        val expiryDate = voucher.voucherExpiryDate?.parseDateToFormat(SHORT_DATE_FORMAT).orEmpty()
        return decorateTextWithFont(
            originalText = getString(R.string.usable_end_date_value, expiryDate),
            wordsToStyle = listOf(expiryDate)
        )
    }

    private fun mapVoucherPrice(
        voucher: Voucher
    ): SpannableString {
        val price = getString(R.string.price_format, voucher.productPrice.orEmpty())
        return decorateTextWithFont(
            originalText = getString(R.string.voucher_value, price),
            wordsToStyle = listOf(price)
        )
    }

    private fun decorateTextWithFont(
        originalText: String,
        wordsToStyle: List<String>
    ) = originalText.decorateWords(
        wordsToDecorate = wordsToStyle.map {
            DecoratedText(
                textToDecorate = it,
                typeface = context?.getFontFromResources(R.font.omnes_semibold_regular)
            )
        }
    )

    private fun mapVoucherStatus(
        voucher: Voucher
    ) = when (voucher.voucherStatus) {
        USABLE -> {
            when (voucher.voucherDeliveryStatus) {
                AVAILABLE -> getString(R.string.voucher_available)
                ON_HOLD -> getString(R.string.voucher_on_hold)
                NON_AVAILABLE -> getString(R.string.voucher_non_available)
                else -> ""
            }
        }
        USED -> getString(R.string.voucher_used)
        CANCELLED -> getString(R.string.voucher_canceled)
        INEXISTENT -> getString(R.string.voucher_inexistent)
        null -> ""
    }

    private fun setUpRequestsDropdownView() {
        vouchersDetailsContainerView?.getRequestSpinner?.adapter = VoucherRequestsAdapter(
            requireContext(),
            (args.requestTypes?.map { voucherRequestType ->
                voucherRequestType.requestTypeLabel
            } ?: emptyList()) + ""
        )
    }

    private val vouchersDetailsViewCallback: VouchersDetailsViewCallback by lazy {
        object : VouchersDetailsViewCallback {
            override fun onBackButtonClicked() {
                navDismiss()
            }

            override fun onRequestSelectedAtIndex(index: Int) {
                args.requestTypes?.get(index)?.let {
                    displayDialogForRequestType(it)
                }
            }

            override fun onOngoingRequestsClicked() {
                findNavController().safeNavigate(
                    VoucherDetailsFragmentDirections.actionVoucherDetailsFragmentToVoucherOngoingRequestListFragment(
                        categoryUI = args.categoryUI,
                        voucherId = args.voucherId
                    )
                )
            }

            override fun onMoreButtonClicked() {
                viewModel.getVoucherEventsHelper.stateData?.let { voucher ->
                    voucher.productWebpage?.let { url ->
                        context?.openWebPage(url)
                    }
                }
            }

            override fun onAmendDatesButtonClicked() {
                viewModel.getVoucherEventsHelper.stateData?.let {
                    displayDialogForUpdateAvailability(
                        it.voucherStartDate,
                        it.voucherExpiryDate
                    )
                }
            }
        }
    }

    private fun displayDialogForRequestType(selectedRequestType: VoucherRequestType) {
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.MaterialAlertDialog_rounded
        ).apply {
            val voucherRequestView = layoutInflater.inflate(
                R.layout.voucher_request_layout,
                null
            ) as VoucherRequestView
            voucherRequestView.fillViewWithData(
                VoucherRequestViewAdapter(
                    title = selectedRequestType.requestTypeLabel
                )
            )
            setView(voucherRequestView)
            setPositiveButton(R.string.send) { _, _ ->
                viewModel.callSendVoucherRequestWithTypeId(
                    voucherRequestTypeId = selectedRequestType.requestTypeId,
                    voucherRequestComment = voucherRequestView.getUserComment()
                )
            }
            setNegativeButton(R.string.cancel, null)
        }
        .show()
    }

    private fun displayDialogForUpdateAvailability(
        startDate: String?,
        endDate: String?,
    ) {
        VoucherAvailabilityDialogManager(
            requireContext()
        ).displayDialogForUpdateAvailability(
            startDate,
            endDate
        ) { newStartDate, newEndDate, comment ->
            viewModel.callSetVoucherAvailabilityDates(
                voucherStartDate = newStartDate,
                voucherEndDate = newEndDate,
                voucherComment = comment
            )
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (viewModel.voucherListShouldRefresh) {
            setFragmentResult(
                REFRESH_VOUCHER_LIST_REQUEST_KEY,
                bundleOf()
            )
        }
    }

    override fun onDestroyView() {
        vouchersDetailsContainerView?.callback = null
        vouchersDetailsContainerView = null
        super.onDestroyView()
    }
}