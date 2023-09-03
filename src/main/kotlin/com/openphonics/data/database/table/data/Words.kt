package com.openphonics.data.database.table.data

import org.jetbrains.exposed.dao.id.IntIdTable

object Words : IntIdTable() {
    var language = reference("language", Languages)
    val phonic = varchar("phonic", length = 100)
    val sound = varchar("sound", length = 300)
    val translatedWord = varchar("translated_word", length = 100)
    val translatedSound = varchar("translated_sound", length = 300)
    val word = varchar("text", length = 100)

    init {
        uniqueIndex(language, word)
    }
}