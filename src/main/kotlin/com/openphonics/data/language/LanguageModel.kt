package com.openphonics.data.language

import com.openphonics.data.flag.Flag
import com.openphonics.data.word.Word
import kotlinx.serialization.Serializable


@Serializable
data class Language (
    val nativeId: String,
    val languageId: String,
    val languageName: String,
    val flag: Flag,
    val id: Int,
)