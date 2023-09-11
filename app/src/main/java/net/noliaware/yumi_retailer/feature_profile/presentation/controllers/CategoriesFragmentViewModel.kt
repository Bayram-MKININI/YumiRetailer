package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.noliaware.yumi_retailer.commun.presentation.EventsHelper
import net.noliaware.yumi_retailer.feature_profile.domain.repository.ProfileRepository
import net.noliaware.yumi_retailer.feature_profile.domain.model.Category
import javax.inject.Inject

@HiltViewModel
class CategoriesFragmentViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    val eventsHelper = EventsHelper<List<Category>>()

    init {
        callGetCategories()
    }

    private fun callGetCategories() {
        viewModelScope.launch {
            profileRepository.getVoucherCategories().onEach { result ->
                eventsHelper.handleResponse(result)
            }.launchIn(this)
        }
    }
}