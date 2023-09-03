package com.openphonics.data.database.table.data

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object Flags : IdTable<String>() {
    val flag = text("flag")
    override val id: Column<EntityID<String>> = char("id", length = 2)
        .entityId().uniqueIndex()
}