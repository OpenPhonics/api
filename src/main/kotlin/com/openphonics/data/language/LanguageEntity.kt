package com.openphonics.data.language

import com.openphonics.data.flag.FlagEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class LanguageEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LanguageEntity>(Languages)
    var nativeId by Languages.nativeId
    var languageId by Languages.languageId
    var languageName by Languages.languageName
    var flag by FlagEntity referencedOn Languages.flag
}