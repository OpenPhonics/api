package com.openphonics.data.entity.data


import com.openphonics.data.database.table.data.Sections
import com.openphonics.data.database.table.data.Units
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction

class EntityUnit(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EntityUnit>(Units)

    var language by EntityLanguage referencedOn Units.language
    var title by Units.title
    var order by Units.order
    private val _sections by lazy {
        EntitySection.find { Sections.unit eq id }
    }

    val sections get() = _sections
}

