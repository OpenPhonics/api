package com.openphonics.data.model.progress

import com.openphonics.data.entity.progress.EntityUnitProgress

data class UnitProgress(
    val title: String,
    val order: Int,
    val sections: List<SectionProgress>,
    val hasData: Boolean,
    val progressId: String,
    val id: String
) {
    companion object {
        const val UNIT: Int = 0
        const val UNIT_WITH_SECTIONS: Int = 1
        const val UNIT_WITH_SECTIONS_WITH_LESSON_DATA: Int = 2

        fun fromEntity(entity: EntityUnitProgress, depth: Int) = UnitProgress(
            entity.unit.title,
            entity.unit.order,
            if (depth > 0)
                entity.sections.map {
                    SectionProgress.fromEntity(it, depth-1)

                }
            else emptyList(),
            depth > 0,
            entity.id.value.toString(),
            entity.unit.id.value.toString()
        )
    }
}