package com.openphonics.data.entity.data


import com.openphonics.data.database.table.data.Sentences
import com.openphonics.data.database.table.references.SentenceWordCrossRefs
import com.openphonics.data.entity.references.EntitySentenceWordCrossRef
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction

class EntitySentence(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EntitySentence>(Sentences)

    private val _words = transaction {
        EntitySentenceWordCrossRef.find { SentenceWordCrossRefs.sentence eq id}
            .mapNotNull { EntityWord.findById(it.word) } }

    var language by EntityLanguage referencedOn Sentences.language
    val words get() = _words
}
