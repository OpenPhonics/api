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
    val words: List<Int>
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

@Serializable
data class FlagRequest(
    val flag: String,
    val id: String
)

@Serializable
data class WordSectionRequest(
    val word: Int
)

@Serializable
data class SentenceSectionRequest(
    val sentence: Int
)

@Serializable
data class UpdateUnitRequest(
    val title: String,
    val order: Int
)

@Serializable
data class UpdateWordRequest(
    val phonic: String,
    val sound: String,
    val translatedSound: String,
    val translatedWord: String,
    val word: String,
)

@Serializable
data class UpdateSentenceRequest(
    val words: List<Int>
)

@Serializable
data class UpdateFlagRequest(
    val flag: String
)