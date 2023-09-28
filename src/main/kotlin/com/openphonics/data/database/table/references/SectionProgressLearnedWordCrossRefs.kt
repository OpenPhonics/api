package com.openphonics.data.database.table.references

import com.openphonics.data.database.table.data.Words
import com.openphonics.data.database.table.progress.SectionsProgress
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object SectionProgressLearnedWordCrossRefs : UUIDTable() {
    val sectionProgress = reference("section_progress", SectionsProgress, onDelete = ReferenceOption.CASCADE)
    val learnedWord = reference("learned_word",SectionWordCrossRefs, onDelete = ReferenceOption.CASCADE)
}
