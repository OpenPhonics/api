package com.openphonics.data.database.table.references

import com.openphonics.data.database.table.data.Sentences
import com.openphonics.data.database.table.data.Words
import org.jetbrains.exposed.dao.id.IntIdTable

object SentenceWordCrossRefs : IntIdTable() {
    val word = reference("word", Words)
    val sentence = reference("sentence", Sentences)

    init {
        uniqueIndex(word, sentence)
    }
}