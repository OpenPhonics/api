package com.openphonics.data.language

import com.openphonics.data.flag.Flags
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Languages : IntIdTable() {
    val nativeId = char("native_id", length = 2)
    val languageId = char("language_id", length = 2)
    val languageName = varchar("language_name", length = 30)
    val flag = reference("flag", Flags, onDelete = ReferenceOption.CASCADE)
    init {
        uniqueIndex(nativeId, languageId)
    }
}