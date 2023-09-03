package com.openphonics.data.database.table.user

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object Classrooms : IdTable<String>() {
    val className = varchar("class_name", length = 30)
    override val id: Column<EntityID<String>> = text("class_code")
        .entityId().uniqueIndex()
}