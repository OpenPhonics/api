package com.openphonics.data.database.table.data

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Sentences : IntIdTable() {
    var language = reference("language", Languages, onDelete = ReferenceOption.CASCADE)
}