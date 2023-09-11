package net.noliaware.yumi_retailer.feature_message.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi_retailer.feature_message.domain.repository.MessageRepository
import javax.inject.Inject

@HiltViewModel
class ReceivedMessagesFragmentViewModel @Inject constructor(
    private val repository: MessageRepository
) : ViewModel() {
    fun getMessages() = repository.getReceivedMessageList().cachedIn(viewModelScope)
}