package com.openphonics.data.dao
import com.openphonics.data.database.table.data.Languages
import com.openphonics.data.database.table.data.Sections
import com.openphonics.data.database.table.data.Units
import com.openphonics.data.entity.data.*
import com.openphonics.data.entity.references.EntitySectionSentenceCrossRef
import com.openphonics.data.entity.references.EntitySectionWordCrossRef
import com.openphonics.data.entity.references.EntitySentenceWordCrossRef
import com.openphonics.data.model.data.*
import com.openphonics.data.model.data.Unit
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

interface DataDao {
    fun addLanguage(nativeId: String, languageId: String, languageName: String, flag: String): Int
    fun addUnit(title: String, order: Int, languageId: Int): Int
    fun addSection(title: String, order: Int, lessonCount: Int, unitId: Int): Int
    fun addWordToSection(wordId: Int, sectionId: Int): Int
    fun removeWordFromSection(wordId: Int, sectionId: Int): Boolean
    fun addSentenceToSection(sentenceId: Int, sectionId: Int): Int
    fun removeSentenceFromSection(sentenceId: Int, sectionId: Int): Boolean
    fun addWord(
        phonic: String,
        sound: String,
        translatedSound: String,
        translatedWord: String,
        word: String,
        languageId: Int
    ): Int

    fun addFlag(flagImg: String, flagId: String): String

    fun getAllFlags(): List<Flag>

    fun getFlagById(flagId: String): Flag?

    fun updateFlag(flagId: String, flagImg: String): String
    fun deleteFlag(flagId: String): Boolean


    fun addSentence(languageId: Int, words: List<Int>): Int
    fun deleteLanguage(languageId: Int): Boolean
    fun deleteUnit(unitId: Int): Boolean
    fun deleteSection(sectionId: Int): Boolean
    fun deleteWord(wordId: Int): Boolean
    fun deleteSentence(sentenceId: Int): Boolean
    fun getLanguage(nativeId: String, languageId: String, depth: Int): Language?
    fun getLanguages(nativeId: String, depth: Int): List<Language>
    fun getLanguageById(languageId: Int, depth: Int): Language?
    fun getUnitById(unitId: Int, depth: Int): Unit?
    fun getSectionById(sectionId: Int, depth: Int): Section?
    fun getWord(languageId: Int, word: String): Word?
    fun getWordById(wordId: Int): Word?
    fun getSentenceById(sentenceId: Int): Sentence?

    fun updateSentence(sentenceId: Int, words: List<Int>): Int
    fun updateWord(wordId: Int, phonic: String, sound: String, translatedSound: String, translateWord: String, text: String): Int
    fun updateSection(sectionId: Int, title: String, order: Int, lessonCount: Int, unitId: Int): Int
    fun updateUnit(unitId: Int, title: String, order: Int): Int
    fun updateLanguage(languageId: Int, native: String, language: String, languageName: String, flag: String): Int

    fun exists(id: Int, type: Any): Boolean

    fun nativeExists(native: String): Boolean

    fun flagExists(flag: String): Boolean

}


@Singleton
class DataDaoImpl @Inject constructor() : DataDao {
    override fun addFlag(flagImg: String, flagId: String): String  = FlagOp.create(flagId, flagImg).id.value
    override fun addLanguage(nativeId: String, languageId: String, languageName: String, flag: String): Int = LanguageOp.create(nativeId, languageId, languageName, flag).id.value
    override fun addUnit(
        title: String,
        order: Int,
        languageId: Int
    ): Int = transaction {
        //Creates new unit
        val unit = UnitOp.create(title, min(updateUnitOrder(order, languageId), order), languageId)
        //Adds new unit to all relevant progress
        val relatedProgress = LanguageProgressOp.all(languageId)
        relatedProgress.forEach{
            UnitProgressOp.create(it.id.value, unit.id.value)
        }
        return@transaction unit.id.value
    }
    override fun addSection(
        title: String,
        order: Int,
        lessonCount: Int,
        unitId: Int
    ): Int = transaction {
        //Creates new section
        val section = SectionOp.create(title, min(updateSectionOrder(order, unitId), order), lessonCount, unitId)
        //Adds new section to all relevant progress
        val relatedProgress = UnitProgressOp.all(unitId)
        relatedProgress.forEach{
            SectionProgressOp.create(it.id.value, section.id.value)
        }
        return@transaction section.id.value
    }
    override fun addWord(
        phonic: String,
        sound: String,
        translatedSound: String,
        translatedWord: String,
        word: String,
        languageId: Int
    ): Int = WordOp.create(languageId, phonic, sound, translatedWord, translatedSound, word).id.value
    override fun addSentence(languageId: Int, words: List<Int>): Int = transaction {
        val sentenceId = SentenceOp.create(languageId).id.value
        words.forEachIndexed { order, wordId ->
            SentenceWordCrossRefOp.add(wordId, sentenceId, order)
        }
        return@transaction sentenceId
    }
    override fun addWordToSection(wordId: Int, sectionId: Int): Int = SectionWordCrossRefOp.add(wordId, sectionId).id.value
    override fun addSentenceToSection(sentenceId: Int, sectionId: Int): Int = SectionSentenceCrossRefOp.add(sentenceId, sectionId).id.value
    override fun getAllFlags(): List<Flag> = FlagOp.all().map { Flag.fromEntity(it) }
    override fun getFlagById(flagId: String): Flag? = FlagOp.get(flagId)?.let{Flag.fromEntity(it)}
    override fun getLanguage(nativeId: String, languageId: String, depth: Int): Language? = LanguageOp.get(nativeId, languageId)?.let {Language.fromEntity(it, depth)}
    override fun getLanguages(nativeId: String, depth: Int): List<Language> = LanguageOp.all(nativeId).map { Language.fromEntity(it, depth) }
    override fun getLanguageById(languageId: Int, depth: Int): Language? = LanguageOp.get(languageId)?.let{ Language.fromEntity(it, depth) }
    override fun getUnitById(unitId: Int, depth: Int): Unit? = UnitOp.get(unitId)?.let { Unit.fromEntity(it, depth) }
    override fun getSectionById(sectionId: Int, depth: Int): Section? = SectionOp.get(sectionId)?.let {Section.fromEntity(it, depth)}
    override fun getWord(languageId: Int, word: String): Word? = WordOp.get(languageId, word)?.let {Word.fromEntity(it)}
    override fun getWordById(wordId: Int): Word? = WordOp.get(wordId)?.let {Word.fromEntity(it)}
    override fun getSentenceById(sentenceId: Int): Sentence? = SentenceOp.get(sentenceId)?.let {Sentence.fromEntity(it)}
    override fun updateFlag(flagId: String, flagImg: String): String = FlagOp.update(flagId, flagImg).id.value
    override fun updateLanguage(
        languageId: Int,
        native: String,
        language: String,
        languageName: String,
        flag: String
    ): Int = LanguageOp.update(languageId, native, language, languageName, flag).id.value
    override fun updateUnit(unitId: Int, title: String, order: Int): Int = transaction {
        val unit = EntityUnit[unitId]
        UnitOp.update(unitId, title, min(updateSectionOrder(order, unitId, unit.order), order))
        return@transaction unitId
    }
    override fun updateSection(sectionId: Int, title: String, order: Int, lessonCount: Int, unitId: Int): Int = transaction {
        val section = EntitySection[sectionId]
        SectionOp.update(sectionId, title, min(updateSectionOrder(order, unitId, section.order, section.unit.id.value), order), lessonCount, unitId)
        val relevantProgress = SectionProgressOp.all(sectionId)
        relevantProgress.forEach {
            SectionProgressOp.update(
                it.id.value,
                currentLesson = min(it.currentLesson, lessonCount),
                unitProgressId = UnitProgressOp.get(unitId, it.unit.language.id.value)?.id?.value
            )
        }
        return@transaction sectionId
    }
    override fun updateWord(
        wordId: Int,
        phonic: String,
        sound: String,
        translatedSound: String,
        translateWord: String,
        text: String
    ): Int = WordOp.update(
        wordId = wordId,
        phonic = phonic,
        sound = sound,
        translatedSound = translatedSound,
        translatedWord = translateWord,
        word = text
    ).id.value
    override fun updateSentence(sentenceId: Int, words: List<Int>): Int = transaction {
        OpenPhonicsEntityOp.delete(SentenceWordCrossRefOp.all(sentenceId))
        words.forEachIndexed {order, wordId ->
            SentenceWordCrossRefOp.add(wordId, sentenceId, order)
        }
        return@transaction sentenceId
    }
    override fun deleteFlag(flagId: String): Boolean = FlagOp.delete(flagId)
    override fun deleteLanguage(languageId: Int): Boolean = LanguageOp.delete(languageId)
    override fun deleteUnit(unitId: Int): Boolean = UnitOp.delete(unitId)
    override fun deleteSection(sectionId: Int): Boolean = SectionOp.delete(sectionId)
    override fun removeWordFromSection(wordId: Int, sectionId: Int): Boolean = SectionWordCrossRefOp.remove(wordId, sectionId)
    override fun removeSentenceFromSection(sentenceId: Int, sectionId: Int): Boolean = SectionSentenceCrossRefOp.remove(sentenceId, sectionId)
    override fun deleteWord(wordId: Int): Boolean = WordOp.delete(wordId)
    override fun deleteSentence(sentenceId: Int): Boolean = SentenceOp.delete(sentenceId)
    override fun exists(id: Int, type: Any): Boolean = transaction {
        when (type) {
            EntityLanguage -> EntityLanguage
            EntityUnit -> EntityUnit
            EntitySection -> EntitySection
            EntitySentence -> EntitySentence
            EntityWord -> EntityWord
            EntitySectionSentenceCrossRef -> EntitySectionSentenceCrossRef
            EntitySectionWordCrossRef -> EntitySectionWordCrossRef
            EntitySentenceWordCrossRef -> EntitySentenceWordCrossRef
            else -> return@transaction false
        }.findById(id) != null
    }
    override fun nativeExists(native: String): Boolean = transaction{
        EntityLanguage.find {
            Languages.nativeId eq native
        }.firstOrNull() != null
    }
    override fun flagExists(flag: String): Boolean = transaction{
        EntityFlag.findById(flag) != null
    }

    private val checkLists: (List<String>, List<String>) -> Boolean = { x: List<String>, y: List<String> ->
        if (x == y) {
            true
        } else {
            x.sorted().toList() == y.sorted().toList()
        }

    }
    private fun updateUnitOrder(order: Int, languageId: Int, prevOrder: Int = Int.MAX_VALUE): Int = transaction {
        var max = 0
        EntityUnit.find {
            (Units.language eq languageId)
        }.map {
            it.apply {
                if (this.order > prevOrder){
                    this.order -=1
                }
                if (this.order >= order) {
                    this.order += 1
                }
            }
            if (it.order > max) max = it.order
        }
        max + 1
    }
    private fun updateSectionOrder(order: Int, unit: Int, prevOrder: Int = Int.MAX_VALUE, prevUnit: Int = Int.MAX_VALUE ): Int = transaction {
        var max = 0
        EntitySection.find {
            (Sections.unit eq prevUnit)
        }.map {
            it.apply {
                if (this.order > prevOrder){
                    this.order -=1
                }

            }
        }
        EntitySection.find {
            (Sections.unit eq unit)
        }.map {
            it.apply {
                if (this.order >= order) {
                    this.order += 1
                }
            }
            if (it.order > max) max = it.order
        }
        max + 1
    }

}