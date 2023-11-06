package com.openphonics.application.request

import kotlinx.serialization.Serializable

@Serializable
data class FlagRequest(
    val flag: String,
    val id: String
)