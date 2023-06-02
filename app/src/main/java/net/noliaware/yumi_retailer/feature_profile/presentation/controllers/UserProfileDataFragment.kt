package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.commun.BO_SIGN_IN_FRAGMENT_TAG
import net.noliaware.yumi_retailer.commun.util.ViewModelState
import net.noliaware.yumi_retailer.commun.util.handleSharedEvent
import net.noliaware.yumi_retailer.commun.util.redirectToLoginScreenFromSharedEvent
import net.noliaware.yumi_retailer.feature_profile.domain.model.UserProfile
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProfileDataParentView
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProfileDataView.ProfileDataViewCallback
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProfileDataView.ProfileViewAdapter

@AndroidEntryPoint
class UserProfileDataFragment : Fragment() {

    private var profileDataParentView: ProfileDataParentView? = null
    private val viewModel by viewModels<UserProfileDataFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_data_layout, container, false).apply {
            profileDataParentView = this as ProfileDataParentView
            profileDataParentView?.getProfileDataView?.callback = profileViewCallback
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
                    is ViewModelState.DataState -> vmState.data?.let { userProfile ->
                        bindViewToData(userProfile)
                    }
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
            address = address
        ).also {
            profileDataParentView?.getProfileDataView?.fillViewWithData(it)
        }
    }

    private val profileViewCallback: ProfileDataViewCallback by lazy {
        ProfileDataViewCallback {
            BOSignInFragment.newInstance().show(
                childFragmentManager.beginTransaction(),
                BO_SIGN_IN_FRAGMENT_TAG
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        profileDataParentView = null
    }
}