package com.openphonics.data.database.table.references


import com.openphonics.data.database.table.data.Sections
import com.openphonics.data.database.table.data.Words
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object SectionWordCrossRefs : IntIdTable() {
    val word = reference("word", Words, onDelete = ReferenceOption.CASCADE)
    val section = reference("section", Sections, onDelete = ReferenceOption.CASCADE)
    init {
        uniqueIndex(word, section)
    }
}
