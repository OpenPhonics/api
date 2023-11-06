package com.openphonics.application.request

import kotlinx.serialization.Serializable

@Serializable
data class WordRequest(
    val phonic: String,
    val sound: String,
    val translatedSound: String,
    val translatedWord: String,
    val word: String,
    val language: Int
)