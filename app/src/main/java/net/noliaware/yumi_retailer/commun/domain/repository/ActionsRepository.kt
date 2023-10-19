package net.noliaware.yumi_retailer.commun.domain.repository

import net.noliaware.yumi_retailer.commun.domain.model.Action

interface ActionsRepository {
    fun performActions(actions: List<Action>)
}