package net.noliaware.yumi_retailer.feature_scan.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi_retailer.commun.Args.ACCOUNT_DATA
import net.noliaware.yumi_retailer.feature_login.domain.model.AccountData
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val accountData get() = savedStateHandle.get<AccountData>(ACCOUNT_DATA)
}