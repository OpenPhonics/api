package com.openphonics.data.entity.data

import com.openphonics.data.database.table.data.Sections
import com.openphonics.data.database.table.references.SectionSentenceCrossRefs
import com.openphonics.data.database.table.references.SectionWordCrossRefs
import com.openphonics.data.entity.references.EntitySectionSentenceCrossRef
import com.openphonics.data.entity.references.EntitySectionWordCrossRef
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction

class EntitySection(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EntitySection>(Sections)

    var unit by EntityUnit referencedOn Sections.unit
    var title by Sections.title
    var lessonCount by Sections.lessonCount
    var order by Sections.order

    private val _words by lazy {
        EntitySectionWordCrossRef.find { SectionWordCrossRefs.section eq id}
            .mapNotNull { EntityWord.findById(it.word) }
    }

    private val _sentences by lazy {
        EntitySectionSentenceCrossRef.find { SectionSentenceCrossRefs.section eq id}
            .mapNotNull { EntitySentence.findById(it.sentence) }
    }

    val words get() = _words
    val sentences get() = _sentences
}

