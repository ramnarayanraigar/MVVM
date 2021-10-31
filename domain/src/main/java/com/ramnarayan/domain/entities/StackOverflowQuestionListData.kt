package com.ramnarayan.domain.entities

import java.io.Serializable

data class StackOverflowQuestionListData(
    val has_more: Boolean,
    val items: List<Item>,
    val quota_max: Int,
    val quota_remaining: Int
) : Serializable