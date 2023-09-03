package com.openphonics.model.request

import kotlinx.serialization.Serializable


@Serializable
data class LanguageProgressRequest(
    val languageId: Int
)
@Serializable
data class XPRequest(
    val xp: Short
)
@Serializable
data class StreakRequest(
    val continueStreak: Boolean
)
@Serializable
data class SectionProgressRequest(
    val currentLesson: Short = 0,
    val isLegendary: Boolean,
    val learnedWords: List<Int>
)