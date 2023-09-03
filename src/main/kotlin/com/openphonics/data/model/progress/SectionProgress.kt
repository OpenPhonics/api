package com.openphonics.data.model.progress

import com.openphonics.data.entity.progress.EntitySectionProgress
import com.openphonics.data.model.data.Sentence
import com.openphonics.data.model.data.Word

data class SectionProgress(
    val currentLesson: Int,
    val isLegendary: Boolean,
    val progressId: String,
    val learnedWords: List<Word>,
    val title: String,
    val order: Int,
    val lessonCount: Int,
    val words: List<Word>,
    val sentences: List<Sentence>,
    val hasData: Boolean,
    val id: String,
)  {
    companion object {
        const val SECTION: Int = 0
        const val SECTIONS_WITH_LESSON_DATA: Int = 1
        fun fromEntity(entity: EntitySectionProgress, depth: Int) = SectionProgress(
            entity.currentLesson,
            entity.isLegendary,
            entity.id.value.toString(),
            if (depth > 0)
                entity.learnedWords.map {
                    Word.fromEntity(it)
                } else emptyList(),
            entity.section.title,
            entity.section.order,
            entity.section.lessonCount,
            if (depth > 0)
                entity.section.words.map {
                    Word.fromEntity(it)
                } else emptyList(),
            if (depth > 0)
                entity.section.sentences.map {
                    Sentence.fromEntity(it)
                } else emptyList(),
            depth > 0,
            entity.section.id.value.toString()
        )
    }
}
