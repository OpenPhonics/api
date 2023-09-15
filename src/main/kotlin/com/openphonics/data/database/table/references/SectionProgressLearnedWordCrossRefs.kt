package com.openphonics.data.database.table.references

import com.openphonics.data.database.table.data.Words
import com.openphonics.data.database.table.progress.SectionsProgress
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object SectionProgressLearnedWordCrossRefs : UUIDTable() {
    val learnedWord = reference("learned_word", Words, onDelete = ReferenceOption.CASCADE)
    val section = reference("section", SectionsProgress, onDelete = ReferenceOption.CASCADE)
    init {
        uniqueIndex(learnedWord, section)
    }
}
