package com.openphonics.model.request

import kotlinx.serialization.Serializable

@Serializable
data class DepthRequest(
    val depth: Int
)