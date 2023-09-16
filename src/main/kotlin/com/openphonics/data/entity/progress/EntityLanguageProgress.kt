package com.openphonics.data.entity.progress

import com.openphonics.data.database.table.progress.LanguagesProgress
import com.openphonics.data.database.table.progress.UnitsProgress
import com.openphonics.data.entity.data.EntityLanguage
import com.openphonics.data.entity.user.EntityUser
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*



class EntityLanguageProgress(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityLanguageProgress>(LanguagesProgress)

    var user by EntityUser referencedOn LanguagesProgress.user
    var language by EntityLanguage referencedOn LanguagesProgress.language
    var started by LanguagesProgress.started
    var streak by LanguagesProgress.streak
    var xp by LanguagesProgress.xp
    var updated by LanguagesProgress.updated
    private val _units by lazy {
        EntityUnitProgress.find { UnitsProgress.language eq id }
    }
    val units get() = _units
}