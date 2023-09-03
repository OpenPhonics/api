package com.openphonics.data.model.data

import com.openphonics.data.entity.data.EntityLanguage


data class Language (
    val nativeId: String,
    val languageId: String,
    val languageName: String,
    val flag: String,
    val units: List<Unit>,
    val hasData: Boolean,
    val id: Int,
) {
    companion object {
        const val LANGUAGE: Int = 0
        const val LANGUAGE_WITH_UNITS: Int = 1
        const val LANGUAGE_WITH_UNITS_WITH_SECTIONS: Int = 2
        const val LANGUAGE_WITH_UNITS_WITH_SECTION_WITH_LESSON_DATA: Int = 3
        fun fromEntity(entity: EntityLanguage, depth: Int) = Language(
            entity.nativeId,
            entity.languageId,
            entity.languageName,
            entity.flag.value,
            if (depth > 0)
                entity.units.map {
                    Unit.fromEntity(it, depth - 1)
                } else emptyList(),
            depth > 0,
            entity.id.value
        )
    }

}