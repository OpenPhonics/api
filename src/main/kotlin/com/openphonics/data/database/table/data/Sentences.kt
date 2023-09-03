package com.openphonics.data.database.table.data

import org.jetbrains.exposed.dao.id.IntIdTable

object Sentences : IntIdTable() {
    var language = reference("language", Languages)
}