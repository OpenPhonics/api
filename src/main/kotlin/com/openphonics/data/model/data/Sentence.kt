package com.openphonics.data.model.data


import com.openphonics.data.entity.data.EntitySentence

data class Sentence(
    val language: Int,
    val sentence: List<Word>,
    val id: Int
) {
    companion object {
        fun fromEntity(entity: EntitySentence) = Sentence(
            entity.language.id.value,
            entity.words.map {
                Word.fromEntity(it)
            },
            entity.id.value
        )
    }
}