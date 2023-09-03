package com.openphonics.data.entity.references
import com.openphonics.data.database.table.references.SectionWordCrossRefs
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


class EntitySectionWordCrossRef(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EntitySectionWordCrossRef>(SectionWordCrossRefs)

    var word by SectionWordCrossRefs.word
    var section by SectionWordCrossRefs.section
}