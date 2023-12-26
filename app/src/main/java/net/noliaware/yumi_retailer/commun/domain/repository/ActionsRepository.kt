package net.noliaware.yumi_retailer.commun.domain.repository

import net.noliaware.yumi_retailer.commun.domain.model.Action

interface ActionsRepository {
    suspend fun performActions(actions: List<Action>)
}