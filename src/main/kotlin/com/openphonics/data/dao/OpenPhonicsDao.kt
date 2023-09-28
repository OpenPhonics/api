package com.openphonics.data.dao

import com.openphonics.data.database.table.data.*
import com.openphonics.data.database.table.progress.LanguagesProgress
import com.openphonics.data.database.table.progress.SectionsProgress
import com.openphonics.data.database.table.progress.UnitsProgress
import com.openphonics.data.database.table.references.SectionProgressLearnedWordCrossRefs
import com.openphonics.data.database.table.references.SectionSentenceCrossRefs
import com.openphonics.data.database.table.references.SectionWordCrossRefs
import com.openphonics.data.database.table.references.SentenceWordCrossRefs
import com.openphonics.data.entity.data.*
import com.openphonics.data.entity.progress.EntityLanguageProgress
import com.openphonics.data.entity.progress.EntitySectionProgress
import com.openphonics.data.entity.progress.EntityUnitProgress
import com.openphonics.data.entity.references.EntitySectionProgressLearnedWordCrossRef
import com.openphonics.data.entity.references.EntitySectionSentenceCrossRef
import com.openphonics.data.entity.references.EntitySectionWordCrossRef
import com.openphonics.data.entity.references.EntitySentenceWordCrossRef
import com.openphonics.data.entity.user.EntityUser
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

object OpenPhonicsEntityOp {
    fun <T : Entity<ID>, ID : Comparable<ID>> delete(entities: List<T>) = transaction {
        for (entity in entities) {
            entity.run {
                delete()
            }
        }
    }
}
object FlagOp {
    fun create(flagId: String, flag: String): EntityFlag = transaction {
        EntityFlag.new(flagId) {
            this.flag = flag
        }
    }
    fun all(): List<EntityFlag> = transaction {
        EntityFlag.all().toList()
    }


    fun update(flagId: String, newFlag: String) = transaction {
        EntityFlag[flagId].apply {
            this.flag = newFlag
        }
    }

    fun get(flagId: String): EntityFlag? = transaction {
        EntityFlag.findById(flagId)
    }

    fun delete(flagId: String): Boolean = transaction {
        get(flagId)?.run {
            delete()
            return@transaction true
        }
        return@transaction false
    }
}
object LanguageOp {
    fun create(nativeId: String, languageId: String, languageName: String, flag: String): EntityLanguage  = transaction {
        EntityLanguage.new {
            this.nativeId = nativeId
            this.languageId = languageId
            this.languageName = languageName
            this.flag = EntityID(flag, Flags)
        }
    }
    fun all(nativeId: String): List<EntityLanguage> = transaction {
        EntityLanguage.find {
            (Languages.nativeId eq nativeId)
        }.toList()
    }
    fun update(language: Int, nativeId: String? = null, languageId: String? = null, languageName: String? = null, flag: String? = null) = transaction {
        EntityLanguage[language].apply {
            nativeId?.let { this.nativeId = it }
            languageId?.let { this.languageId = it }
            languageName?.let { this.languageName = it }
            flag?.let { this.flag = EntityID(it, Flags) }
        }
    }
    fun get(languageId: Int): EntityLanguage? = transaction {
        EntityLanguage.findById(languageId)
    }
    fun get(nativeId: String, languageId: String): EntityLanguage? = transaction {
        EntityLanguage.find {
            ((Languages.nativeId eq nativeId) and (Languages.languageId eq languageId))
        }.firstOrNull()
    }
    fun delete(languageId: Int): Boolean = transaction {
        get(languageId)?.run {
            delete()
            return@transaction true
        }
        return@transaction false
    }
}
object UnitOp {
    fun create(title: String, order: Int, languageId: Int): EntityUnit = transaction {
        EntityUnit.new {
            this.title = title
            this.order = order
            this.language = EntityLanguage[languageId]
        }
    }
    fun update(unitId: Int, title: String? = null, order: Int? = null, languageId: Int? = null) = transaction {
        EntityUnit[unitId].apply {
            title?.let { this.title = it }
            order?.let { this.order = it }
            languageId?.let { this.language = EntityLanguage[it] }
        }
    }
    fun get(unitId: Int): EntityUnit? = transaction {
        EntityUnit.findById(unitId)
    }
    fun get(nativeId: String, languageId: String, order: Int): EntityUnit? = transaction {
        LanguageOp.get(nativeId, languageId)?.let {
            it.units.forEach {unit->
                if (order == unit.order) return@transaction unit
            }
        }
        return@transaction null
    }
    fun delete(unitId: Int): Boolean = transaction {
        get(unitId)?.run {
            delete()
            return@transaction true
        }
        return@transaction false
    }
}
object SectionWordCrossRefOp {
    fun add(wordId: Int, sectionId: Int): EntitySectionWordCrossRef = transaction {
        EntitySectionWordCrossRef.new {
            this.word = EntityID(wordId, Words)
            this.section = EntityID(sectionId, Sections)
        }
    }
    fun get(wordId: Int, sectionId: Int): EntitySectionWordCrossRef? = transaction {
        EntitySectionWordCrossRef.find {
            ((SectionWordCrossRefs.section eq sectionId) and (SectionWordCrossRefs.word eq wordId))
        }.firstOrNull()
    }
    fun remove(wordId: Int, sectionId: Int): Boolean= transaction {
        get(wordId, sectionId)?.run{
            delete()
            return@transaction true
        }
        return@transaction false
    }
}
object SectionSentenceCrossRefOp {
    fun add(sentenceId: Int, sectionId: Int): EntitySectionSentenceCrossRef = transaction {
        EntitySectionSentenceCrossRef.new {
            this.sentence = EntityID(sentenceId, Sentences)
            this.section = EntityID(sectionId, Sections)
        }
    }
    fun get(sentenceId: Int, sectionId: Int): EntitySectionSentenceCrossRef? = transaction {
        EntitySectionSentenceCrossRef.find {
            ((SectionSentenceCrossRefs.section eq sectionId) and (SectionSentenceCrossRefs.sentence eq sentenceId))
        }.firstOrNull()
    }
    fun remove(sentenceId: Int, sectionId: Int): Boolean= transaction {
        get(sentenceId, sectionId)?.run{
            delete()
            return@transaction true
        }
        return@transaction false
    }
}
object SectionOp {
    fun create(title: String, order: Int, lessonCount: Int, unitId: Int): EntitySection = transaction {
        EntitySection.new {
            this.title = title
            this.order = order
            this.lessonCount = lessonCount
            this.unit = EntityUnit[unitId]
        }
    }
    fun update(sectionId: Int, title: String? = null, order: Int? = null, lessonCount: Int? = null, unitId: Int? = null) = transaction {
        EntitySection[sectionId].apply {
            title?.let { this.title = it }
            order?.let { this.order = it }
            lessonCount?.let { this.lessonCount = it }
            unitId?.let { this.unit = EntityUnit[it] }
        }
    }
    fun get(sectionId: Int): EntitySection? = transaction {
        EntitySection.findById(sectionId)
    }
    fun get(nativeId: String, languageId: String, unitOrder: Int, sectionOrder: Int): EntitySection? = transaction {
        UnitOp.get(nativeId, languageId, unitOrder)?.let {unit->
            unit.sections.forEach { section ->
                if (sectionOrder == section.order) return@transaction section
            }
        }
        return@transaction null
    }
    fun delete(sectionId: Int): Boolean = transaction {
        get(sectionId)?.run {
            delete()
            return@transaction true
        }
        return@transaction false
    }
}
object WordOp {
    fun create(languageId: Int, phonic: String, sound: String, translatedWord: String, translatedSound: String, word: String): EntityWord = transaction {
        EntityWord.new {
            this.phonic = phonic
            this.sound = sound
            this.translatedWord = translatedWord
            this.translatedSound = translatedSound
            this.word = word
            this.language = EntityLanguage[languageId]
        }
    }
    fun update(
        wordId: Int,
        languageId: Int? = null,
        phonic: String? = null,
        sound: String? = null,
        translatedWord: String? = null,
        translatedSound: String? = null,
        word: String? = null
    ) = transaction {
        EntityWord[wordId].apply {
            languageId?.let { this.language = EntityLanguage[it] }
            phonic?.let { this.phonic = it }
            sound?.let { this.sound = it }
            translatedWord?.let { this.translatedWord = it }
            translatedSound?.let { this.translatedSound = it }
            word?.let { this.word = it }
        }
    }
    fun get(wordId: Int): EntityWord? = transaction {
        EntityWord.findById(wordId)
    }
    fun get(languageId: Int, text: String): EntityWord? = transaction {
        EntityWord.find {
            ((Words.language eq languageId) and (Words.word eq text))
        }.firstOrNull()
    }
    fun delete(wordId: Int): Boolean = transaction {
        get(wordId)?.run {
            delete()
            return@transaction true
        }
        return@transaction false
    }

}
object SentenceOp {
    fun create(languageId: Int): EntitySentence = transaction{
        EntitySentence.new {
            this.language = EntityLanguage[languageId]
        }
    }
    fun get(sentenceId: Int): EntitySentence? = transaction {
        EntitySentence.findById(sentenceId)
    }
    fun delete(sentenceId: Int): Boolean = transaction {
        get(sentenceId)?.run {
            delete()
            return@transaction true
        }
        return@transaction false
    }
}
object SentenceWordCrossRefOp {

    fun add(wordId: Int, sentenceId: Int, order: Int): EntitySentenceWordCrossRef = transaction {
        EntitySentenceWordCrossRef.new {
            this.word = EntityID(wordId, Words)
            this.order = order
            this.sentence = EntityID(sentenceId, Sentences)
        }
    }
    fun remove(wordId: Int, sentenceId: Int): Boolean= transaction {
        EntitySentenceWordCrossRef.find {
            ((SentenceWordCrossRefs.sentence eq sentenceId) and (SentenceWordCrossRefs.sentence eq wordId))
        }.firstOrNull()?.run{
            delete()
            return@transaction true
        }
        return@transaction false
    }
    fun all(sentenceId: Int): List<EntitySentenceWordCrossRef> = transaction {
        EntitySentenceWordCrossRef.find {
            (SentenceWordCrossRefs.sentence eq sentenceId)
        }.toList()
    }
}
object LanguageProgressOp {
    fun create(userId: UUID, languageId: Int): EntityLanguageProgress = transaction {
        EntityLanguageProgress.new {
            this.user = EntityUser[userId]
            this.language = EntityLanguage[languageId]
        }
    }

    fun update(languageProgressId: UUID, xp: Int? = null, started: DateTime? = null, streak: Int? = null, updated: DateTime? = null) = transaction {
        EntityLanguageProgress[languageProgressId].apply {
            started?.let {this.started = it}
            xp?.let { this.xp = it }
            streak?.let { this.streak = it }
            updated?.let { this.updated = it }
        }
    }
    fun all(languageId: Int): List<EntityLanguageProgress> = transaction {
        EntityLanguageProgress.find {
            LanguagesProgress.language eq languageId
        }.toList()
    }
    fun all(user: UUID): List<EntityLanguageProgress> = transaction {
        EntityLanguageProgress.find {
            LanguagesProgress.user eq user
        }.toList()
    }

    fun get(languageProgressId: UUID): EntityLanguageProgress? = transaction {
        EntityLanguageProgress.findById(languageProgressId)
    }
    fun get(languageId: Int, user: UUID): EntityLanguageProgress? = transaction {
        EntityLanguageProgress.find {
            ((LanguagesProgress.language eq languageId) and (LanguagesProgress.user eq user))
        }.firstOrNull()
    }

    fun delete(languageProgressId: UUID): Boolean = transaction {
        get(languageProgressId)?.run {
            delete()
            return@transaction true
        }
        return@transaction false
    }
}
object UnitProgressOp {
    fun create(languageProgressId: UUID, unitId: Int): EntityUnitProgress = transaction {
        EntityUnitProgress.new {
            this.language = EntityLanguageProgress[languageProgressId]
            this.unit = EntityUnit[unitId]
        }
    }
    fun all(unitId: Int): List<EntityUnitProgress> = transaction {
        EntityUnitProgress.find {
            UnitsProgress.unit eq unitId
        }.toList()
    }

    fun update(unitProgressId: UUID, languageProgressId: UUID? = null, unitId: Int? = null) = transaction {
        EntityUnitProgress[unitProgressId].apply {
            languageProgressId?.let { this.language = EntityLanguageProgress[it] }
            unitId?.let { this.unit = EntityUnit[it] }
        }
    }

    fun get(unitProgressId: UUID): EntityUnitProgress? = transaction {
        EntityUnitProgress.findById(unitProgressId)
    }
    fun get(unitId: Int, languageProgress: UUID): EntityUnitProgress? = transaction {
        EntityUnitProgress.find {
            ((UnitsProgress.language eq languageProgress) and (UnitsProgress.unit eq unitId))
        }.firstOrNull()
    }

    fun delete(unitProgressId: UUID): Boolean = transaction {
        get(unitProgressId)?.run {
            delete()
            return@transaction true
        }
        return@transaction false
    }
}
object SectionProgressOp {
    fun create(unitProgressId: UUID, sectionId: Int): EntitySectionProgress = transaction {
        EntitySectionProgress.new {
            this.unit = EntityUnitProgress[unitProgressId]
            this.section = EntitySection[sectionId]
        }
    }

    fun all(sectionId: Int): List<EntitySectionProgress> = transaction {
        EntitySectionProgress.find {
            SectionsProgress.section eq sectionId
        }.toList()
    }
    fun update(sectionProgressId: UUID, unitProgressId: UUID? = null, sectionId: Int? = null, currentLesson: Int? = null, isLegendary: Boolean? = null) = transaction {
        EntitySectionProgress[sectionProgressId].apply {
            unitProgressId?.let { this.unit = EntityUnitProgress[it] }
            sectionId?.let { this.section = EntitySection[it] }
            currentLesson?.let { this.currentLesson = it }
            isLegendary?.let { this.isLegendary = it }
        }
    }

    fun get(sectionProgressId: UUID): EntitySectionProgress? = transaction {
        EntitySectionProgress.findById(sectionProgressId)
    }

    fun delete(sectionProgressId: UUID): Boolean = transaction {
        get(sectionProgressId)?.run {
            delete()
            return@transaction true
        }
        return@transaction false
    }
}
object SectionProgressLearnedWordsCrossRefOp {
    fun add(progressId: UUID, crossRefId: Int): EntitySectionProgressLearnedWordCrossRef = transaction {
        EntitySectionProgressLearnedWordCrossRef.new {
            this.learnedWord = EntityID(crossRefId, SectionWordCrossRefs)
            this.sectionProgress = EntityID(progressId, SectionsProgress)
        }
    }
    fun get(crossRefId: UUID): EntitySectionProgressLearnedWordCrossRef? = transaction {
        EntitySectionProgressLearnedWordCrossRef.findById(crossRefId)
    }

    fun all(crossRefId: Int): List<EntitySectionProgressLearnedWordCrossRef> = transaction {
        EntitySectionProgressLearnedWordCrossRef.find {
            SectionProgressLearnedWordCrossRefs.learnedWord eq crossRefId
        }.toList()
    }
    fun remove(crossRefId: UUID): Boolean = transaction {
        get(crossRefId)?.run{
            delete()
            return@transaction true
        }
        return@transaction false
    }
}