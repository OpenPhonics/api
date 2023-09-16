package com.openphonics.data.entity.data

import com.openphonics.data.database.table.data.Languages
import com.openphonics.data.database.table.data.Units
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction


class EntityLanguage(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EntityLanguage>(Languages)
    var nativeId by Languages.nativeId
    var languageId by Languages.languageId
    var languageName by Languages.languageName
    var flag by Languages.flag

    private val _units by lazy {
        EntityUnit.find { Units.language eq id }
    }

    val units get() = _units
}