package com.openphonics.data.database.table.references

import com.openphonics.data.database.table.data.Sentences
import com.openphonics.data.database.table.data.Words
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object SentenceWordCrossRefs : IntIdTable() {
    val word = reference("word", Words, onDelete = ReferenceOption.CASCADE)
    val sentence = reference("sentence", Sentences, onDelete = ReferenceOption.CASCADE)
    val order = integer("order")

    init {
        uniqueIndex(word, sentence, order)
    }
}