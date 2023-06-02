package net.noliaware.yumi_retailer.feature_message.presentation.controllers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi_retailer.feature_message.data.repository.MessageRepository
import javax.inject.Inject

@HiltViewModel
class SentMessagesFragmentViewModel @Inject constructor(
    private val repository: MessageRepository
) : ViewModel() {
    fun getMessages() = repository.getSentMessageList().cachedIn(viewModelScope)
}