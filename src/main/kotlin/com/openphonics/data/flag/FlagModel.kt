package com.openphonics.data.flag

import kotlinx.serialization.Serializable

@Serializable
data class Flag(
    val flag: String,
    val id: String
)