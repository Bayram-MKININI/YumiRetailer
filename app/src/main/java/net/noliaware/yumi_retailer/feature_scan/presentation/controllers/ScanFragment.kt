package net.noliaware.yumi_retailer.feature_scan.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.ViewModelState
import net.noliaware.yumi_retailer.commun.util.handleSharedEvent
import net.noliaware.yumi_retailer.commun.util.inflate
import net.noliaware.yumi_retailer.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi_retailer.feature_scan.presentation.views.ScanView
import net.noliaware.yumi_retailer.feature_scan.presentation.views.ScanView.ScanViewCallback

@AndroidEntryPoint
class ScanFragment : Fragment() {

    companion object {
        fun newInstance() = ScanFragment()
    }

    private var scanView: ScanView? = null
    private val viewModel by viewModels<ScanFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.scan_layout)?.apply {
            scanView = this as ScanView
            scanView?.callback = scanViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsHelper.eventFlow.collectLatest { sharedEvent ->
                handleSharedEvent(sharedEvent)
                redirectToLoginScreenFromSharedEvent(sharedEvent)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsHelper.stateFlow.collect { vmState ->
                when (vmState) {
                    is ViewModelState.LoadingState -> Unit
                    is ViewModelState.DataState -> vmState.data?.let {
                    }
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
            MaterialAlertDialogBuilder(it)
                .setTitle(R.string.scan_finished)
                .setMessage(R.string.use_scanned_voucher)
                .setPositiveButton(R.string.validate) { _, _ ->
                    if ((activity as MainActivity).checkIfCameraPermissionIsGranted()) {
                        viewModel.callUseVoucherByCode(scannedQRCode)
                    }
                }.setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }.setCancelable(false)
                .create()
                .apply {
                    setCanceledOnTouchOutside(false)
                    show()
                }
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
        super.onDestroyView()
        scanView = null
    }
}