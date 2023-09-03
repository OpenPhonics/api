package com.openphonics.data.dao

import com.openphonics.data.database.table.progress.LanguagesProgress
import com.openphonics.data.database.table.progress.SectionsProgress
import com.openphonics.data.database.table.progress.UnitsProgress
import com.openphonics.data.database.table.references.SectionProgressLearnedWordCrossRefs
import com.openphonics.data.database.table.user.Users
import com.openphonics.data.entity.data.EntityLanguage
import com.openphonics.data.entity.data.EntityWord
import com.openphonics.data.entity.progress.EntityLanguageProgress
import com.openphonics.data.entity.progress.EntitySectionProgress
import com.openphonics.data.entity.progress.EntityUnitProgress
import com.openphonics.data.entity.references.EntitySectionProgressLearnedWordCrossRef
import com.openphonics.data.entity.user.EntityUser
import com.openphonics.data.model.progress.LanguageProgress
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
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
    override fun getLanguageProgress(languageProgressId: String, depth: Int): LanguageProgress? = transaction {
        EntityLanguageProgress.findById(UUID.fromString(languageProgressId))?.let {
            LanguageProgress.fromEntity(it, depth)
        }
    }
    override fun addLanguageProgress(userId: String, languageId: Int): String = transaction {
        val language = EntityLanguage[languageId]
        val user = EntityUser[UUID.fromString(userId)]
        val languageProgress = EntityLanguageProgress.new {
            this.language = language
            this.user = user
        }
        val unitsProgress = language.units.map {
            EntityUnitProgress.new {
                this.language = languageProgress
                this.unit = it
            }
        }.associateBy { it.unit }

        unitsProgress.map { (unit, unitProgress) ->
            unit.sections.map { section ->
                EntitySectionProgress.new {
                    this.unit = unitProgress
                    this.section = section
                }
            }
        }
        return@transaction languageProgress.id.value.toString()

    }
    override fun getLanguageProgressByUser(userId: String, depth: Int): List<LanguageProgress> = transaction {
        EntityLanguageProgress
            .find {
                LanguagesProgress.user eq UUID.fromString(userId)
            }.sortedWith(compareBy { it.started })
            .reversed()
            .map { LanguageProgress.fromEntity(it, depth) }
    }
    override fun updateLanguageProgress(languageId: String, xp: Int): String = transaction {
        EntityLanguageProgress[UUID.fromString(languageId)].apply {
            this.xp = xp
        }.id.value.toString()
    }

    override fun deleteLanguageProgress(languageProgressId: String): Boolean = transaction {
        val eLanguageProgress = EntityLanguageProgress.findById(UUID.fromString(languageProgressId))

        eLanguageProgress?.run {
            eLanguageProgress.units.map { unit ->
                unit.sections.map { section ->
                    EntitySectionProgressLearnedWordCrossRef.find {
                        (SectionProgressLearnedWordCrossRefs.section eq section.id)
                    }.forEach { ref ->
                        ref.run {
                            delete()
                        }
                    }
                    section.run {
                        delete()
                    }
                }
                unit.run {
                    delete()
                }
            }
            delete()
            return@transaction true
        }
        return@transaction false
    }

    override fun updateStreak(languageId: String, streakContinued: Boolean): String = transaction {
        EntityLanguageProgress[UUID.fromString(languageId)].apply {
            this.streak = if (streakContinued) this.streak + 1 else 0
            this.updated = DateTime.now()
        }.id.value.toString()
    }

    override fun updateUnitProgress(unitId: String): String {
        TODO("Not yet implemented")
    }

    override fun updateSectionProgress(
        sectionProgressId: String,
        currentLesson: Int,
        isLegendary: Boolean,
        learnedWordIds: List<Int>
    ): String {
        learnedWordIds.forEach { wordId ->
            addLearnedWord(wordId, sectionProgressId)
        }
        return EntitySectionProgress[UUID.fromString(sectionProgressId)].apply {
            this.currentLesson = currentLesson
            this.isLegendary = isLegendary
        }.id.value.toString()
    }

    private fun addLearnedWord(wordId: Int, sectionProgressId: String) {
        EntitySectionProgressLearnedWordCrossRef.new {
            this.section = EntitySectionProgress[UUID.fromString(sectionProgressId)].id
            this.learnedWord = EntityWord[wordId].id
        }
    }

    override fun exists(id: String, type: Any): Boolean = transaction {
        when (type::class) {
            EntityLanguageProgress::class -> EntityLanguageProgress
            EntitySectionProgress::class -> EntitySectionProgress
            EntityUnitProgress::class -> EntityUnitProgress
            EntitySectionProgressLearnedWordCrossRef::class -> EntitySectionProgressLearnedWordCrossRef
            else -> throw IllegalArgumentException("Unsupported progress type: ${type::class.simpleName}")
        }.findById(UUID.fromString(id)) != null
    }

    override fun isOwnedByUser(progressId: String, userId: String, type: Any): Boolean = transaction {
        when (type::class) {
            EntityLanguageProgress::class ->
                (LanguagesProgress innerJoin Users)
                    .slice(LanguagesProgress.id)
                    .select {
                        (LanguagesProgress.id eq UUID.fromString(progressId)) and (Users.id eq UUID.fromString(userId))
                    }
                    .count() > 0

            EntityUnitProgress::class ->
                (UnitsProgress innerJoin LanguagesProgress innerJoin Users)
                    .slice(UnitsProgress.id)
                    .select {
                        (UnitsProgress.id eq UUID.fromString(progressId)) and (Users.id eq UUID.fromString(userId))
                    }
                    .count() > 0

            EntitySectionProgress::class ->
                (SectionsProgress innerJoin UnitsProgress innerJoin LanguagesProgress innerJoin Users)
                    .slice(SectionsProgress.id)
                    .select {
                        (SectionsProgress.id eq UUID.fromString(progressId)) and (Users.id eq UUID.fromString(userId))
                    }
                    .count() > 0



            else -> false
        }
    }
}