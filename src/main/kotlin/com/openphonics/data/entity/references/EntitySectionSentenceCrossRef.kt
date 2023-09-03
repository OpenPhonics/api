package com.openphonics.data.entity.references

import com.openphonics.data.database.table.references.SectionSentenceCrossRefs
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID


class EntitySectionSentenceCrossRef(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EntitySectionSentenceCrossRef>(SectionSentenceCrossRefs)

    var sentence by SectionSentenceCrossRefs.sentence
    var section by SectionSentenceCrossRefs.section
}