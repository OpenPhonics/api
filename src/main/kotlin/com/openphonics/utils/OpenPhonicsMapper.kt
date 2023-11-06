package com.openphonics.utils

import com.openphonics.data.flag.Flag
import com.openphonics.data.flag.FlagEntity
import com.openphonics.data.language.Language
import com.openphonics.data.language.LanguageEntity
import com.openphonics.data.word.Word
import com.openphonics.data.word.WordEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OpenPhonicsMapper @Inject constructor(){

    fun fromEntity(entity: FlagEntity): Flag = Flag(
        entity.flag,
        entity.id.value
    )
    fun fromEntity(entity: LanguageEntity) = Language(
        entity.nativeId,
        entity.languageId,
        entity.languageName,
        fromEntity(entity.flag),
        entity.words.map {fromEntity(it)},
        entity.id.value
    )
    fun fromEntity(entity: WordEntity) = Word(
        entity.language.id.value,
        entity.phonic,
        entity.sound,
        entity.translatedSound,
        entity.translatedWord,
        entity.word,
        entity.id.value
    )
}