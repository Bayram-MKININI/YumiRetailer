package net.noliaware.yumi_retailer.feature_scan.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.ViewState.DataState
import net.noliaware.yumi_retailer.commun.util.ViewState.LoadingState
import net.noliaware.yumi_retailer.commun.util.collectLifecycleAware
import net.noliaware.yumi_retailer.commun.util.handleSharedEvent
import net.noliaware.yumi_retailer.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi_retailer.feature_login.presentation.controllers.MainActivity
import net.noliaware.yumi_retailer.feature_scan.presentation.views.ScanView
import net.noliaware.yumi_retailer.feature_scan.presentation.views.ScanView.ScanViewCallback

@AndroidEntryPoint
class ScanFragment : Fragment() {

    private var scanView: ScanView? = null
    private val viewModel by viewModels<ScanFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.scan_layout,
        container,
        false
    )?.apply {
        scanView = this as ScanView
        scanView?.callback = scanViewCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
    }

    private fun collectFlows() {
        viewModel.eventsHelper.eventFlow.collectLifecycleAware(viewLifecycleOwner) { sharedEvent ->
            handleSharedEvent(sharedEvent)
            redirectToLoginScreenFromSharedEvent(sharedEvent)
        }
        viewModel.eventsHelper.stateFlow.collectLifecycleAware(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is LoadingState -> Unit
                is DataState -> viewState.data?.let {
                }
            }
        }
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        result.contents?.let { scannedQRCode ->
            displayVoucherScannedDialog(scannedQRCode)
        }
    }

    private fun displayVoucherScannedDialog(
        scannedQRCode: String
    ) {
        context?.let {
            MaterialAlertDialogBuilder(it, R.style.AlertDialog)
                .setTitle(R.string.scan_finished)
                .setMessage(R.string.use_scanned_voucher)
                .setPositiveButton(R.string.validate) { _, _ ->
                    if ((activity as MainActivity).checkIfCameraPermissionIsGranted()) {
                        viewModel.callUseVoucherByCode(scannedQRCode)
                    }
                }.setNegativeButton(R.string.cancel, null)
                .setCancelable(false)
                .show()
        }
    }

    private fun scanBarcodeCustomLayout() {
        val options = ScanOptions()
        options.captureActivity = PortraitCaptureActivity::class.java
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt(getString(R.string.scan_voucher))
        options.setOrientationLocked(true)
        options.setBeepEnabled(false)
        barcodeLauncher.launch(options)
    }

    private val scanViewCallback: ScanViewCallback by lazy {
        ScanViewCallback { scanBarcodeCustomLayout() }
    }

    override fun onDestroyView() {
        scanView?.callback = null
        scanView = null
        super.onDestroyView()
    }
}