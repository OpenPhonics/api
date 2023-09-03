package com.openphonics.data.entity.progress

import com.openphonics.data.database.table.progress.SectionsProgress
import com.openphonics.data.database.table.progress.UnitsProgress
import com.openphonics.data.entity.data.EntityUnit
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class EntityUnitProgress(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityUnitProgress>(UnitsProgress)

    var language by EntityLanguageProgress referencedOn UnitsProgress.language
    var unit by EntityUnit referencedOn  UnitsProgress.unit
    private val _sections by lazy {
        transaction {
            EntitySectionProgress.find { SectionsProgress.unit eq id }
        }
    }
    val sections get() = _sections
}