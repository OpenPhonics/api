package com.openphonics.data.model.data

import com.openphonics.data.entity.data.EntitySection


data class Section (
    val title: String,
    val order: Int,
    val lessonCount: Int,
    val words: List<Word>,
    val sentences: List<Sentence>,
    val hasData: Boolean,
    val unit: Int,
    val id: Int
) {
    companion object {
        const val SECTION: Int = 0
        const val SECTIONS_WITH_LESSON_DATA: Int = 1
        fun fromEntity(entity: EntitySection, depth: Int) = Section(
            entity.title,
            entity.order,
            entity.lessonCount,
            if (depth > 0)
                entity.words.map {
                    Word.fromEntity(it)
                } else emptyList(),
            if (depth > 0)
                entity.sentences.map {
                    Sentence.fromEntity(it)
                } else emptyList(),
            depth > 0,
            entity.unit.id.value,
            entity.id.value
        )
    }
}
