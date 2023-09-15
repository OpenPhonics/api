package com.openphonics.data.database.table.progress

import com.openphonics.data.database.table.data.Units
import com.openphonics.data.database.table.user.Users
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object UnitsProgress : UUIDTable() {
    val language = reference("language", LanguagesProgress, onDelete = ReferenceOption.CASCADE)
    val unit = reference("unit", Units, onDelete = ReferenceOption.CASCADE)

    init {
        uniqueIndex(language, unit)
    }
}
