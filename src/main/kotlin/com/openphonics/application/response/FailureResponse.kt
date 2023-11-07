package com.openphonics.application.response

import kotlinx.serialization.Serializable

@Serializable
data class FailureResponse(override val status: State, override val message: String) : Response