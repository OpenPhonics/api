package com.openphonics.data.word

import com.openphonics.data.language.Languages
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object Words : IntIdTable() {
    var language = reference("language", Languages, onDelete = ReferenceOption.CASCADE)
    val phonic = varchar("phonic", length = 100)
    val sound = varchar("sound", length = 300)
    val translatedWord = varchar("translated_word", length = 100)
    val translatedSound = varchar("translated_sound", length = 300)
    val word = varchar("word", length = 100)

    init {
        uniqueIndex(language, word)
    }
}