package com.openphonics.data.entity.user

import com.openphonics.data.database.table.user.Classrooms
import com.openphonics.data.database.table.user.Users
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

class EntityClassroom(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, EntityClassroom>(Classrooms)

    var className by Classrooms.className
    val students by EntityUser referrersOn Users.classCode

}