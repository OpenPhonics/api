package com.openphonics.data.model.references

import com.openphonics.data.entity.references.EntitySectionWordCrossRef


data class SectionWordCrossRefData(
    val wordId: String,
    val sectionId: String,
    val id: Int,
) {
    companion object {
        fun fromEntity(entity: EntitySectionWordCrossRef) = SectionWordCrossRefData(
            entity.word.value.toString(),
            entity.section.value.toString(),
            entity.id.value
        )
    }
}