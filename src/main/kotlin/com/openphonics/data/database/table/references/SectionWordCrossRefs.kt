package com.openphonics.data.database.table.references


import com.openphonics.data.database.table.data.Sections
import com.openphonics.data.database.table.data.Words
import org.jetbrains.exposed.dao.id.IntIdTable

object SectionWordCrossRefs : IntIdTable() {
    val word = reference("word", Words)
    val section = reference("section", Sections)
    init {
        uniqueIndex(word, section)
    }
}
