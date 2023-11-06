package com.openphonics.data.flag

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class FlagEntity(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, FlagEntity>(Flags)
    var flag by Flags.flag
}