package net.noliaware.yumi_retailer.feature_scan.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.Args.ACCOUNT_DATA
import net.noliaware.yumi_retailer.commun.FragmentTags.PRIVACY_POLICY_FRAGMENT_TAG
import net.noliaware.yumi_retailer.commun.util.inflate
import net.noliaware.yumi_retailer.commun.util.withArgs
import net.noliaware.yumi_retailer.feature_alerts.presentation.controllers.AlertsFragment
import net.noliaware.yumi_retailer.feature_login.domain.model.AccountData
import net.noliaware.yumi_retailer.feature_message.presentation.controllers.MessagingFragment
import net.noliaware.yumi_retailer.feature_profile.presentation.controllers.UserProfileFragment
import net.noliaware.yumi_retailer.feature_scan.presentation.views.HomeMenuView
import net.noliaware.yumi_retailer.feature_scan.presentation.views.HomeView
import java.time.Duration

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        fun newInstance(
            accountData: AccountData
        ) = HomeFragment().withArgs(ACCOUNT_DATA to accountData)
    }

    private var homeView: HomeView? = null
    private val viewModel by viewModels<HomeFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return container?.inflate(R.layout.home_layout)?.apply {
            homeView = this as HomeView
            homeView?.homeMenuView?.callback = homeMenuViewCallback
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayScanFragment()
        viewModel.accountData?.let { accountData ->
            homeView?.homeMenuView?.let { homeMenuView ->
                if (accountData.newMessageCount > 0) {
                    homeMenuView.setBadgeForMailButton(accountData.newMessageCount)
                }
                if (accountData.newAlertCount > 0) {
                    homeMenuView.setBadgeForNotificationButton(accountData.newAlertCount)
                }
            }
            if (accountData.shouldConfirmPrivacyPolicy) {
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(Duration.ofMillis(150))
                    PrivacyPolicyFragment.newInstance(
                        privacyPolicyUrl = accountData.privacyPolicyUrl,
                        isConfirmationRequired = true
                    ).show(
                        childFragmentManager.beginTransaction(),
                        PRIVACY_POLICY_FRAGMENT_TAG
                    )
                }
            }
        }
    }

    private val homeMenuViewCallback: HomeMenuView.HomeMenuViewCallback by lazy {
        object : HomeMenuView.HomeMenuViewCallback {
            override fun onCategoryButtonClicked() {
                displayScanFragment()
            }

            override fun onProfileButtonClicked() {
                childFragmentManager.beginTransaction().run {
                    replace(
                        R.id.main_fragment_container,
                        UserProfileFragment()
                    )
                    commit()
                }
            }

            override fun onMailButtonClicked() {
                homeView?.homeMenuView?.hideMailButtonBadge()
                childFragmentManager.beginTransaction().run {
                    replace(
                        R.id.main_fragment_container,
                        MessagingFragment.newInstance(viewModel.accountData?.messageSubjects)
                    )
                    commit()
                }
            }

            override fun onNotificationButtonClicked() {
                homeView?.homeMenuView?.hideNotificationButtonBadge()
                childFragmentManager.beginTransaction().run {
                    replace(R.id.main_fragment_container, AlertsFragment())
                    commit()
                }
            }
        }
    }

    private fun displayScanFragment() {
        childFragmentManager.beginTransaction().run {
            replace(
                R.id.main_fragment_container,
                ScanFragment.newInstance()
            )
            commit()
            homeView?.selectHomeButton()
        }
    }

    override fun onDestroyView() {
        homeView = null
        super.onDestroyView()
    }
}