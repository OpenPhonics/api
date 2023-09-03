package com.openphonics.data.database.table.data

import org.jetbrains.exposed.dao.id.IntIdTable

object Languages : IntIdTable() {
    val nativeId = char("native_id", length = 2)
    val languageId = char("language_id", length = 2)
    val languageName = varchar("language_name", length = 30)
    val flag = reference("flag", Flags)

    init {
        uniqueIndex(nativeId, languageId)
    }
}