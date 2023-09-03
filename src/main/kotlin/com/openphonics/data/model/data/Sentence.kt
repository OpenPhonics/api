package com.openphonics.data.model.data


import com.openphonics.data.entity.data.EntitySentence

data class Sentence(
    val sentence: List<Word>,
    val id: Int
) {
    companion object {
        fun fromEntity(entity: EntitySentence) = Sentence(
            entity.words.map {
                Word.fromEntity(it)
            },
            entity.id.value
        )
    }
}