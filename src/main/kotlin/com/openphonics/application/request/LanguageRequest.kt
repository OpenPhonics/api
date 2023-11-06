package com.openphonics.application.request

import kotlinx.serialization.Serializable

@Serializable
data class LanguageRequest(
    val nativeId: String,
    val languageId: String,
    val languageName: String,
    val flag: String,
)
@Serializable
data class UpdateLanguageRequest(
    val nativeId: String? = null,
    val languageId: String? = null,
    val languageName: String? = null,
    val flag: String? = null,
)
