package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi_retailer.commun.Args.CATEGORY
import net.noliaware.yumi_retailer.feature_profile.domain.model.Category
import javax.inject.Inject

@HiltViewModel
class VouchersOverviewFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val category get() = savedStateHandle.get<Category>(CATEGORY)
}