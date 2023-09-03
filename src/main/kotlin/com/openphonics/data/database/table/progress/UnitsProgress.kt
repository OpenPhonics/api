package com.openphonics.data.database.table.progress

import com.openphonics.data.database.table.data.Units
import com.openphonics.data.database.table.user.Users
import org.jetbrains.exposed.dao.id.UUIDTable

object UnitsProgress : UUIDTable() {
    val language = reference("language", LanguagesProgress)
    val unit = reference("unit", Units)

    init {
        uniqueIndex(language, unit)
    }
}
