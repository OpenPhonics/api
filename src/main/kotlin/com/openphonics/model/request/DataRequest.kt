package com.openphonics.model.request

import kotlinx.serialization.Serializable

@Serializable
data class LanguageRequest(
    val nativeId: String,
    val languageId: String,
    val languageName: String,
    val flag: String,
)

@Serializable
data class UnitRequest(
    val title: String,
    val order: Int,
    val languageId: Int
)

@Serializable
data class SectionRequest(
    val title: String,
    val order: Int,
    val lessonCount: Int,
    val unitId: Int
)

@Serializable
data class SentenceRequest(
    val languageId: Int,
    val words: String
)

@Serializable
data class WordRequest(
    val phonic: String,
    val sound: String,
    val translatedSound: String,
    val translatedWord: String,
    val word: String,
    val languageId: Int
)
