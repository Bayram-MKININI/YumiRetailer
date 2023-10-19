package net.noliaware.yumi_retailer.commun.domain.model

data class Action(
    val type: String = "",
    val params: List<ActionParam>
)