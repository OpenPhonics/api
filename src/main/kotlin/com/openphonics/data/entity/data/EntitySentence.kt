package com.openphonics.data.entity.data


import com.openphonics.data.database.table.data.Sentences
import com.openphonics.data.database.table.references.SectionWordCrossRefs
import com.openphonics.data.database.table.references.SentenceWordCrossRefs
import com.openphonics.data.entity.references.EntitySectionWordCrossRef
import com.openphonics.data.entity.references.EntitySentenceWordCrossRef
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class EntitySentence(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EntitySentence>(Sentences)
    var language by EntityLanguage referencedOn Sentences.language
    private val _words by EntitySentenceWordCrossRef referrersOn SentenceWordCrossRefs.sentence
    private val sorted_words by lazy {
            transaction {
                _words.sortedBy { it.order }.mapNotNull {EntityWord.findById(it.word)}
            }
        }
    val words get() =sorted_words
}
