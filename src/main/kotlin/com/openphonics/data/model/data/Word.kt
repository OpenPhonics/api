package com.openphonics.data.model.data

import com.openphonics.data.entity.data.EntityWord

data class Word(
    val phonic: String,
    val sound: String,
    val translatedSound: String,
    val translatedWord: String,
    val word: String,
    val id: Int
) {
    companion object {
        fun fromEntity(entity: EntityWord) = Word(
            entity.phonic,
            entity.sound,
            entity.translatedSound,
            entity.translatedWord,
            entity.word,
            entity.id.value
        )
    }
}
