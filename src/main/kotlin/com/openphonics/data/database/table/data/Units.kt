package com.openphonics.data.database.table.data

import org.jetbrains.exposed.dao.id.IntIdTable

object Units : IntIdTable() {
    val language = reference("language", Languages)
    val title = varchar("title", length = 30)
    val order = integer("order")

    init {
        uniqueIndex(language, order)
    }
}