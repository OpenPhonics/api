package com.openphonics.data.word

import com.openphonics.data.language.LanguageEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class WordEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WordEntity>(Words)

    var language by LanguageEntity referencedOn Words.language
    var phonic by Words.phonic
    var sound by Words.sound
    var translatedWord by Words.translatedWord
    var translatedSound by Words.translatedSound
    var word by Words.word
}