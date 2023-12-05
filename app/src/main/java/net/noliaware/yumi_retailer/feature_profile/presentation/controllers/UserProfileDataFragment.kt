package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.Args.ACCOUNT_DATA
import net.noliaware.yumi_retailer.commun.util.ViewState.DataState
import net.noliaware.yumi_retailer.commun.util.ViewState.LoadingState
import net.noliaware.yumi_retailer.commun.util.collectLifecycleAware
import net.noliaware.yumi_retailer.commun.util.handleSharedEvent
import net.noliaware.yumi_retailer.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi_retailer.commun.util.safeNavigate
import net.noliaware.yumi_retailer.commun.util.withArgs
import net.noliaware.yumi_retailer.feature_login.domain.model.AccountData
import net.noliaware.yumi_retailer.feature_login.domain.model.TFAMode
import net.noliaware.yumi_retailer.feature_profile.domain.model.UserProfile
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProfileDataParentView
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProfileDataView.ProfileDataViewCallback
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProfileDataView.ProfileViewAdapter

@AndroidEntryPoint
class UserProfileDataFragment : Fragment() {

    companion object {
        fun newInstance(
            accountData: AccountData?
        ) = UserProfileDataFragment().withArgs(ACCOUNT_DATA to accountData)
    }

    private var profileDataParentView: ProfileDataParentView? = null
    private val viewModel by viewModels<UserProfileDataFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.profile_data_layout,
        container,
        false
    ).apply {
        profileDataParentView = this as ProfileDataParentView
        profileDataParentView?.getProfileDataView?.callback = profileViewCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileDataParentView?.activateLoading(true)
        collectFlows()
    }

    private fun collectFlows() {
        viewModel.eventsHelper.eventFlow.collectLifecycleAware(viewLifecycleOwner) { sharedEvent ->
            profileDataParentView?.activateLoading(false)
            handleSharedEvent(sharedEvent)
            redirectToLoginScreenFromSharedEvent(sharedEvent)
        }
        viewModel.eventsHelper.stateFlow.collectLifecycleAware(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is LoadingState -> Unit
                is DataState -> viewState.data?.let { userProfile ->
                    profileDataParentView?.activateLoading(false)
                    bindViewToData(userProfile)
                }
            }
        }
    }

    private fun bindViewToData(userProfile: UserProfile) {
        val address = StringBuilder().apply {
            append(userProfile.address)
            append(getString(R.string.new_line))
            if (userProfile.addressComplement.isNotBlank()) {
                append(userProfile.addressComplement)
                append(getString(R.string.new_line))
            }
            append(userProfile.postCode)
            append(" ")
            append(userProfile.city)
        }.toString()

        ProfileViewAdapter(
            login = userProfile.login,
            retailer = userProfile.label,
            email = userProfile.email,
            phone = userProfile.cellPhoneNumber,
            address = address,
            twoFactorAuthModeText = map2FAModeText(viewModel.accountData?.twoFactorAuthMode),
            twoFactorAuthModeActivated = map2FAModeActivation(viewModel.accountData?.twoFactorAuthMode)
        ).also {
            profileDataParentView?.getProfileDataView?.fillViewWithData(it)
        }
    }

    private fun map2FAModeText(
        twoFactorAuthMode: TFAMode?
    ) = when (twoFactorAuthMode) {
        TFAMode.APP -> getString(R.string.bo_two_factor_auth_by_app)
        TFAMode.MAIL -> getString(R.string.bo_two_factor_auth_by_mail)
        else -> getString(R.string.bo_two_factor_auth_none)
    }

    private fun map2FAModeActivation(
        twoFactorAuthMode: TFAMode?
    ) = when (twoFactorAuthMode) {
        TFAMode.APP -> true
        else -> false
    }

    private val profileViewCallback: ProfileDataViewCallback by lazy {
        object : ProfileDataViewCallback {
            override fun onGetCodeButtonClicked() {
                findNavController().safeNavigate(
                    UserProfileFragmentDirections.actionUserProfileFragmentToBOSignInFragment()
                )
            }

            override fun onPrivacyPolicyButtonClicked() {
                findNavController().safeNavigate(
                    UserProfileFragmentDirections.actionUserProfileFragmentToPrivacyPolicyFragment(
                        privacyPolicyUrl = viewModel.accountData?.privacyPolicyUrl.orEmpty(),
                        isPrivacyPolicyConfirmationRequired = false
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        profileDataParentView = null
        super.onDestroyView()
    }
}