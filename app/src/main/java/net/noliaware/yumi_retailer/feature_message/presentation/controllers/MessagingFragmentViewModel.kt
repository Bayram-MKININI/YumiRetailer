package net.noliaware.yumi_retailer.feature_message.presentation.controllers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import net.noliaware.yumi_retailer.commun.Args.MESSAGE_SUBJECTS_DATA
import net.noliaware.yumi_retailer.feature_login.domain.model.MessageSubject
import javax.inject.Inject

@HiltViewModel
class MessagingFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val messageSubjects get() = savedStateHandle.get<List<MessageSubject>>(MESSAGE_SUBJECTS_DATA)
}