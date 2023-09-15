package com.openphonics.data.database.table.references


import com.openphonics.data.database.table.data.Sections
import com.openphonics.data.database.table.data.Sentences
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object SectionSentenceCrossRefs : IntIdTable() {
    val sentence = reference("sentence", Sentences, onDelete = ReferenceOption.CASCADE)
    val section = reference("section", Sections, onDelete = ReferenceOption.CASCADE)

    init {
        uniqueIndex(sentence, section)
    }
}