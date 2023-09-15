package com.openphonics.data.entity.references

import com.openphonics.data.database.table.references.SentenceWordCrossRefs
import com.openphonics.data.entity.data.EntityWord
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class EntitySentenceWordCrossRef(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EntitySentenceWordCrossRef>(SentenceWordCrossRefs)

    var word by SentenceWordCrossRefs.word
    var sentence by SentenceWordCrossRefs.sentence
    var order by SentenceWordCrossRefs.order
}