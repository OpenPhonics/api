package com.openphonics.data.database.table.progress

import com.openphonics.data.database.table.data.Sections
import com.openphonics.data.database.table.user.Users
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object SectionsProgress : UUIDTable() {
    val unit = reference("unit", UnitsProgress, onDelete = ReferenceOption.CASCADE)
    val section = reference("section", Sections, onDelete = ReferenceOption.CASCADE)
    val currentLesson = integer("current_lesson").default(0)
    val isLegendary = bool("is_legendary").default(false)

    init {
        uniqueIndex(unit, section)
    }
}
