package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import net.noliaware.yumi_retailer.R
import net.noliaware.yumi_retailer.feature_login.domain.model.AccountData
import net.noliaware.yumi_retailer.feature_profile.presentation.views.ProfileView

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private var profileView: ProfileView? = null
    private val args by navArgs<UserProfileFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_layout, container, false)?.apply {
            profileView = this as ProfileView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        UserProfileFragmentStateAdapter(
            args.accountData,
            childFragmentManager,
            viewLifecycleOwner.lifecycle
        ).apply {
            profileView?.getViewPager?.adapter = this
        }
    }

    override fun onDestroyView() {
        profileView = null
        super.onDestroyView()
    }

    private class UserProfileFragmentStateAdapter(
        val accountData: AccountData,
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle
    ) : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount() = 3
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> UserProfileDataFragment.newInstance(accountData)
                1 -> CategoriesFragment()
                else -> ProductCategoriesFragment()
            }
        }
    }
}