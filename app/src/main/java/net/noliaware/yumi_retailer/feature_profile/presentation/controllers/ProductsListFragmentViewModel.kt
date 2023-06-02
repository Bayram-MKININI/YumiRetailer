package net.noliaware.yumi_retailer.feature_profile.presentation.controllers

import android.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi_retailer.commun.CATEGORY_COLOR
import net.noliaware.yumi_retailer.commun.CATEGORY_ICON
import net.noliaware.yumi_retailer.commun.CATEGORY_ID
import net.noliaware.yumi_retailer.feature_profile.data.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class ProductsListFragmentViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val categoryColor get() = savedStateHandle.get<Int>(CATEGORY_COLOR) ?: Color.TRANSPARENT
    val categoryIcon get() = savedStateHandle.get<String>(CATEGORY_ICON)
    private val selectedCategoryId get() = savedStateHandle.get<String>(CATEGORY_ID).orEmpty()

    fun getProducts() = repository.getProductListByCategory(selectedCategoryId).cachedIn(viewModelScope)
}