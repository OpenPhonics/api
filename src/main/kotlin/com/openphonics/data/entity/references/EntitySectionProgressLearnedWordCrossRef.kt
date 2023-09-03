package com.openphonics.data.entity.references

import com.openphonics.data.database.table.references.SectionProgressLearnedWordCrossRefs
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class EntitySectionProgressLearnedWordCrossRef(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntitySectionProgressLearnedWordCrossRef>(SectionProgressLearnedWordCrossRefs)
    var learnedWord by SectionProgressLearnedWordCrossRefs.learnedWord
    var section by SectionProgressLearnedWordCrossRefs.section
}