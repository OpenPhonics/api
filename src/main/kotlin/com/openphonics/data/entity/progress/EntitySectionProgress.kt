package com.openphonics.data.entity.progress

import com.openphonics.data.database.table.progress.SectionsProgress
import com.openphonics.data.database.table.references.SectionProgressLearnedWordCrossRefs
import com.openphonics.data.entity.data.EntitySection
import com.openphonics.data.entity.data.EntityWord
import com.openphonics.data.entity.references.EntitySectionProgressLearnedWordCrossRef
import com.openphonics.data.entity.user.EntityUser
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class EntitySectionProgress(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<EntitySectionProgress>(SectionsProgress)

    var unit by EntityUnitProgress referencedOn SectionsProgress.unit
    var currentLesson by SectionsProgress.currentLesson
    var isLegendary by SectionsProgress.isLegendary
    var section by EntitySection referencedOn SectionsProgress.section
    private val _learnedWords by lazy {
        EntitySectionProgressLearnedWordCrossRef.find { SectionProgressLearnedWordCrossRefs.section eq id }
            .mapNotNull { EntityWord.findById(it.learnedWord) }
    }

    val learnedWords get() = _learnedWords
}