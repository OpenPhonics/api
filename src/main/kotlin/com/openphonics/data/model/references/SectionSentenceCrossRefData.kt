package com.openphonics.data.model.references

import com.openphonics.data.entity.references.EntitySectionSentenceCrossRef


data class SectionSentenceCrossRefData(
    val sentenceId: String,
    val sectionId: String,
    val id: Int,
){
    companion object {
        fun fromEntity(entity: EntitySectionSentenceCrossRef) = SectionSentenceCrossRefData(
            entity.sentence.value.toString(),
            entity.section.value.toString(),
            entity.id.value
        )
    }
}