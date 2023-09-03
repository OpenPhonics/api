package com.openphonics.data.model.references

import com.openphonics.data.entity.references.EntitySectionProgressLearnedWordCrossRef

data class SectionProgressLearnedWordCrossRefData(
    val learnedWordId: String,
    val sectionId: String,
    val id: String,
) {
    companion object {
        fun fromEntity(entity: EntitySectionProgressLearnedWordCrossRef) = SectionProgressLearnedWordCrossRefData(
            entity.learnedWord.value.toString(),
            entity.section.value.toString(),
            entity.id.value.toString()
        )
    }
}