package com.openphonics.data.dao

import com.openphonics.data.database.table.progress.LanguagesProgress
import com.openphonics.data.database.table.progress.SectionsProgress
import com.openphonics.data.database.table.progress.UnitsProgress
import com.openphonics.data.database.table.references.SectionProgressLearnedWordCrossRefs
import com.openphonics.data.database.table.references.SectionWordCrossRefs
import com.openphonics.data.database.table.user.Users
import com.openphonics.data.entity.data.EntityLanguage
import com.openphonics.data.entity.data.EntityWord
import com.openphonics.data.entity.progress.EntityLanguageProgress
import com.openphonics.data.entity.progress.EntitySectionProgress
import com.openphonics.data.entity.progress.EntityUnitProgress
import com.openphonics.data.entity.references.EntitySectionProgressLearnedWordCrossRef
import com.openphonics.data.entity.references.EntitySectionWordCrossRef
import com.openphonics.data.entity.user.EntityUser
import com.openphonics.data.model.progress.LanguageProgress
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface ProgressDao : LanguageProgressDao, UnitProgressDao, SectionProgressDao {

    fun exists(id: String, type: Any): Boolean

    fun isOwnedByUser(progressId: String, userId: String, type: Any): Boolean
}

interface LanguageProgressDao {
    //Creates a new language progress
    fun addLanguageProgress(
        userId: String,
        languageId: Int,
    ): String

    //Gets all language progress associated with user
    fun getLanguageProgressByUser(
        userId: String,
        depth: Int
    ): List<LanguageProgress>

    //Gets a single language progress
    fun getLanguageProgress(
        languageProgressId: String,
        depth: Int
    ): LanguageProgress?

    //Updates language progress
    fun updateLanguageProgress(
        languageId: String,
        xp: Int
    ): String

    fun deleteLanguageProgress(
        languageProgressId: String
    ): Boolean


    fun updateStreak(
        languageId: String,
        streakContinued: Boolean
    ): String
    fun languageProgressExists(languageId: Int, userId: String): Boolean
}

interface UnitProgressDao {

    //Updates unit progress
    fun updateUnitProgress(
        unitId: String,
    ): String

}

interface SectionProgressDao {
    //Updates Section progress
    fun updateSectionProgress(
        sectionProgressId: String,
        currentLesson: Int,
        isLegendary: Boolean,
        learnedWordIds: List<Int>
    ): String

}
@Singleton
class ProgressDaoImpl @Inject constructor(
) : ProgressDao {
    override fun addLanguageProgress(userId: String, languageId: Int): String = transaction {
        val language = EntityLanguage[languageId]
        val user = EntityUser[UUID.fromString(userId)]
        val languageProgress = LanguageProgressOp.create(user.id.value, language.id.value)
        val unitsProgress = language.units.map {
            UnitProgressOp.create(languageProgress.id.value, it.id.value)
        }.associateBy { it.unit }
        unitsProgress.map { (unit, unitProgress) ->
            unit.sections.map { section ->
                SectionProgressOp.create(unitProgress.id.value, section.id.value)
            }
        }
        return@transaction languageProgress.id.value.toString()

    }
    override fun getLanguageProgress(languageProgressId: String, depth: Int): LanguageProgress? = LanguageProgressOp.get(UUID.fromString(languageProgressId))?.let {LanguageProgress.fromEntity(it, depth)}
    override fun languageProgressExists(languageId: Int, userId: String): Boolean = LanguageProgressOp.get(languageId, UUID.fromString(userId)) != null
    override fun getLanguageProgressByUser(userId: String, depth: Int): List<LanguageProgress> = LanguageProgressOp.all(UUID.fromString(userId))
           .sortedWith(compareBy { it.started })
           .reversed()
           .map { LanguageProgress.fromEntity(it, depth) }
    override fun updateLanguageProgress(languageId: String, xp: Int): String = LanguageProgressOp.update(UUID.fromString(languageId), xp = xp).id.value.toString()
    override fun updateStreak(languageId: String, streakContinued: Boolean): String = transaction {
        val progress = EntityLanguageProgress[UUID.fromString(languageId)]
        return@transaction LanguageProgressOp.update(
            progress.id.value,
            streak = if (streakContinued) progress.streak + 1 else 0,
            updated = DateTime.now()).id.value.toString()
    }
    override fun updateUnitProgress(unitId: String): String {
        TODO("Not yet implemented")
    }
    override fun updateSectionProgress(
        sectionProgressId: String,
        currentLesson: Int,
        isLegendary: Boolean,
        learnedWordIds: List<Int>
    ): String = transaction{
        val progress = EntitySectionProgress[UUID.fromString(sectionProgressId)]
        learnedWordIds.forEach {wordId ->
            val crossRef = SectionWordCrossRefOp.get(wordId, progress.section.id.value)!!
            SectionProgressLearnedWordsCrossRefOp.add(UUID.fromString(sectionProgressId), crossRef.id.value)
        }
        SectionProgressOp.update(progress.id.value, currentLesson = currentLesson, isLegendary = isLegendary)
        return@transaction sectionProgressId
    }
    override fun deleteLanguageProgress(languageProgressId: String): Boolean = LanguageProgressOp.delete(UUID.fromString(languageProgressId))
    override fun exists(id: String, type: Any): Boolean = transaction {
        when (type) {
            EntityLanguageProgress -> EntityLanguageProgress
            EntitySectionProgress -> EntitySectionProgress
            EntityUnitProgress -> EntityUnitProgress
            EntitySectionProgressLearnedWordCrossRef -> EntitySectionProgressLearnedWordCrossRef
            else -> return@transaction false
        }.findById(UUID.fromString(id)) != null
    }
    override fun isOwnedByUser(progressId: String, userId: String, type: Any): Boolean = transaction {
        when (type) {
            EntityLanguageProgress -> EntityLanguageProgress[UUID.fromString(progressId)].user.id.value.toString() == userId
            EntityUnitProgress ->EntityUnitProgress[UUID.fromString(progressId)].language.user.id.value.toString() == userId
            EntitySectionProgress ->EntitySectionProgress[UUID.fromString(progressId)].unit.language.user.id.value.toString() == userId
            else -> false
        }
    }
}