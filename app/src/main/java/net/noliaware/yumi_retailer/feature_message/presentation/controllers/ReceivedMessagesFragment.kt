package net.noliaware.yumi_retailer.feature_message.presentation.controllers

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
import net.noliaware.yumi_retailer.commun.READ_MESSAGE_FRAGMENT_TAG
import net.noliaware.yumi_retailer.commun.presentation.adapters.ListLoadStateAdapter
import net.noliaware.yumi_retailer.commun.util.handlePaginationError
import net.noliaware.yumi_retailer.feature_message.presentation.adapters.MessageAdapter
import net.noliaware.yumi_retailer.feature_message.presentation.views.MessagesListView

@AndroidEntryPoint
class ReceivedMessagesFragment : Fragment() {

    private var messagesListView: MessagesListView? = null
    private val viewModel by viewModels<ReceivedMessagesFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.messages_list_layout, container, false).apply {
            messagesListView = this as MessagesListView
            messagesListView?.adapter = MessageAdapter { message ->
                ReadInboxMailFragment.newInstance(
                    message.messageId
                ).apply {
                    onReceivedMessageListRefreshed = {
                        messagesListView?.getMessageAdapter?.refresh()
                    }
                }.show(
                    childFragmentManager.beginTransaction(),
                    READ_MESSAGE_FRAGMENT_TAG
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            messagesListView?.getMessageAdapter?.loadStateFlow?.collectLatest { loadState ->
                handlePaginationError(loadState)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getMessages().collectLatest {
                messagesListView?.getMessageAdapter?.withLoadStateFooter(
                    footer = ListLoadStateAdapter()
                )
                messagesListView?.getMessageAdapter?.submitData(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        messagesListView = null
    }
}