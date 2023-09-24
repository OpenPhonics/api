package com.openphonics.data.model.data
import com.openphonics.data.entity.data.EntityUnit

data class Unit(
    val title: String,
    val order: Int,
    val sections: List<Section>,
    val hasData: Boolean,
    val language: Int,
    val id: Int
){
    companion object {
        const val UNIT: Int = 0
        const val UNIT_WITH_SECTIONS: Int = 1
        const val UNIT_WITH_SECTIONS_WITH_LESSON_DATA: Int = 2
        fun fromEntity(entity: EntityUnit, depth: Int) = Unit(
            entity.title,
            entity.order,
            if (depth > 0)
                entity.sections.map {
                    Section.fromEntity(it, depth - 1)
                }
            else emptyList(),
            depth > 0,
            entity.language.id.value,
            entity.id.value
        )
    }
}