package com.openphonics.data.model.references

import com.openphonics.data.entity.references.EntitySentenceWordCrossRef


data class SentenceWordCrossRefData(
    val wordId: String,
    val sentenceId: String,
    val id: Int,
) {
    companion object {
        fun fromEntity(entity: EntitySentenceWordCrossRef) = SentenceWordCrossRefData(
            entity.word.value.toString(),
            entity.sentence.value.toString(),
            entity.id.value
        )
    }
}