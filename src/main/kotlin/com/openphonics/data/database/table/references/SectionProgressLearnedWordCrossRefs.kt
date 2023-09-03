package com.openphonics.data.database.table.references

import com.openphonics.data.database.table.data.Words
import com.openphonics.data.database.table.progress.SectionsProgress
import org.jetbrains.exposed.dao.id.UUIDTable

object SectionProgressLearnedWordCrossRefs : UUIDTable() {
    val learnedWord = reference("learned_word", Words)
    val section = reference("section", SectionsProgress)
    init {
        uniqueIndex(learnedWord, section)
    }
}
