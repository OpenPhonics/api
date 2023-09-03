package com.openphonics.data.entity.data

import com.openphonics.data.database.table.data.Words
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class EntityWord(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EntityWord>(Words)

    var language by EntityLanguage referencedOn Words.language
    var phonic by Words.phonic
    var sound by Words.sound
    var translatedWord by Words.translatedWord
    var translatedSound by Words.translatedSound
    var word by Words.word
}
