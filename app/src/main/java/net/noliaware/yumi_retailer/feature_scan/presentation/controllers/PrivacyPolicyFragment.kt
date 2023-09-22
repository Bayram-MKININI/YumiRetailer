package net.noliaware.yumi_retailer.feature_scan.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.util.ViewModelState
import net.noliaware.yumi_retailer.commun.util.handleSharedEvent
import net.noliaware.yumi_retailer.commun.util.isNetworkReachable
import net.noliaware.yumi_retailer.commun.util.navDismiss
import net.noliaware.yumi_retailer.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi_retailer.feature_scan.presentation.views.PrivacyPolicyView
import net.noliaware.yumi_retailer.feature_scan.presentation.views.PrivacyPolicyView.PrivacyPolicyViewCallback

@AndroidEntryPoint
class PrivacyPolicyFragment : AppCompatDialogFragment() {

    private var privacyPolicyView: PrivacyPolicyView? = null
    private val args: PrivacyPolicyFragmentArgs by navArgs()
    private val viewModel by viewModels<PrivacyPolicyViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme)
        isCancelable = !args.isPrivacyPolicyConfirmationRequired
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.privacy_policy_layout, container, false).apply {
            privacyPolicyView = this as PrivacyPolicyView
            privacyPolicyView?.callback = privacyPolicyViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
        if (isNetworkReachable(requireContext())) {
            privacyPolicyView?.showWebView()
            privacyPolicyView?.loadURL(args.privacyPolicyUrl)
        } else {
            privacyPolicyView?.showOfflineMessage()
        }
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.privacyPolicyStatusEventsHelper.eventFlow.collectLatest { sharedEvent ->
                handleSharedEvent(sharedEvent)
                redirectToLoginScreenFromSharedEvent(sharedEvent)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.privacyPolicyStatusEventsHelper.stateFlow.collectLatest { vmState ->
                when (vmState) {
                    is ViewModelState.LoadingState -> Unit
                    is ViewModelState.DataState -> vmState.data?.let { result ->
                        if (result) {
                            navDismiss()
                        }
                    }
                }
            }
        }
    }

    private val privacyPolicyViewCallback: PrivacyPolicyViewCallback by lazy {
        PrivacyPolicyViewCallback {
            if (args.isPrivacyPolicyConfirmationRequired) {
                viewModel.callUpdatePrivacyPolicyReadStatus()
            } else {
                navDismiss()
            }
        }
    }

    override fun onDestroyView() {
        privacyPolicyView?.destroyWebPage()
        privacyPolicyView = null
        super.onDestroyView()
    }
}