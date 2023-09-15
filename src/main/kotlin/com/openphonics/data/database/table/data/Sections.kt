package com.openphonics.data.database.table.data

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Sections : IntIdTable() {
    val unit = reference("unit", Units, onDelete = ReferenceOption.CASCADE)
    val title = varchar("title", length = 30)
    val lessonCount = integer("lesson_count")
    val order = integer("order")

    init {
        uniqueIndex(unit, order)
    }
}