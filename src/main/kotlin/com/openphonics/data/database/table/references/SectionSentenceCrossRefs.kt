package com.openphonics.data.database.table.references


import com.openphonics.data.database.table.data.Sections
import com.openphonics.data.database.table.data.Sentences
import org.jetbrains.exposed.dao.id.IntIdTable

object SectionSentenceCrossRefs : IntIdTable() {
    val sentence = reference("sentence", Sentences)
    val section = reference("section", Sections)

    init {
        uniqueIndex(sentence, section)
    }
}