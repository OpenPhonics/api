package com.openphonics.data.entity.data

import com.openphonics.data.database.table.data.Flags
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class EntityFlag(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, EntityFlag>(Flags)

    var flag by Flags.flag
}