package com.openphonics.data.model.progress

import com.openphonics.data.entity.progress.EntityLanguageProgress


data class LanguageProgress(
    val progressId: String,
    val started: Long,
    val updated: Long,
    val streak: Int,
    val xp: Int,
    val nativeId: String,
    val languageId: String,
    val languageName: String,
    val flag: String,
    val units: List<UnitProgress>,
    val hasData: Boolean,
    val id: String
) {
    companion object {
        const val LANGUAGE: Int = 0
        const val LANGUAGE_WITH_UNITS: Int = 1
        const val LANGUAGE_WITH_UNITS_WITH_SECTIONS: Int = 2
        const val LANGUAGE_WITH_UNITS_WITH_SECTION_WITH_LESSON_DATA: Int = 3
        fun fromEntity(entity: EntityLanguageProgress, depth:Int) = LanguageProgress(
            entity.id.value.toString(),
            entity.started.millis,
            entity.updated.millis,
            entity.streak,
            entity.xp,
            entity.language.nativeId,
            entity.language.languageId,
            entity.language.languageName,
            entity.language.flag.value,
            if (depth > 0)
                entity.units.map {
                    UnitProgress.fromEntity(it, depth-1)
                } else emptyList(),
            depth > 0,
            entity.language.id.value.toString()
        )
    }
}
