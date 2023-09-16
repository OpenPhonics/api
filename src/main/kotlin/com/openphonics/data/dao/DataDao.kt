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
import com.openphonics.data.model.data.*
import com.openphonics.data.model.data.Unit
import io.ktor.server.plugins.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.and
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
    fun getUnit(nativeId: String, languageId: String, unitNum: Int, depth: Int): Unit?
    fun getUnitById(unitId: Int, depth: Int): Unit?
    fun getSection(
        nativeId: String,
        languageId: String,
        unitNum: Int,
        sectionNum: Int,
        depth: Int
    ): Section?

    fun getSectionById(sectionId: Int, depth: Int): Section?
    fun getWord(languageId: Int, word: String): Word?
    fun getWordById(wordId: Int): Word?
    fun getSentence(nativeId: String, languageId: String, sentence: String): Sentence?
    fun getSentenceById(sentenceId: Int): Sentence?

    fun updateSentence(sentenceId: Int, words: List<Int>): Int
    fun updateWord(wordId: Int, phonic: String, sound: String, translatedSound: String, translateWord: String, text: String): Int
    fun updateSection(sectionId: Int, title: String, order: Int, lessonCount: Int, unitId: Int): Int
    fun updateUnit(unitId: Int, title: String, order: Int): Int
    fun updateLanguage(languageId: Int, native: String, language: String, languageName: String, flag: String): Int

    fun exists(id: Int, type: Any): Boolean

    fun nativeExists(native: String): Boolean

    fun flagExists(flag: String): Boolean

    fun wordExists(word: String, language: Int): Boolean
}



@Singleton
class DataDaoImpl @Inject constructor() : DataDao {
    override fun addLanguage(nativeId: String, languageId: String, languageName: String, flag: String): Int = transaction {
        EntityLanguage.new {
            this.nativeId = nativeId
            this.languageId = languageId
            this.languageName = languageName
            this.flag = EntityID(flag, Flags)
        }.id.value
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
    private fun updateSectionOrder(order: Int, unitId: Int, prevOrder: Int = Int.MAX_VALUE): Int = transaction {
        var max = 0
        EntitySection.find {
            (Sections.unit eq unitId)
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
    override fun addUnit(
        title: String,
        order: Int,
        languageId: Int
    ): Int = transaction {
        val max = updateUnitOrder(order, languageId)
        val unit = EntityUnit.new {
            this.title = title
            this.order = min(max, order)
            this.language = EntityLanguage[languageId]
        }
        EntityLanguageProgress.find {
            LanguagesProgress.language eq languageId
        }.forEach {languageProgress ->
            EntityUnitProgress.new {
                this.language = languageProgress
                this.unit = unit
            }
        }
        unit.id.value
    }
    override fun addSection(
        title: String,
        order: Int,
        lessonCount: Int,
        unitId: Int
    ): Int = transaction {
        val max = updateSectionOrder(order, unitId)
        val section = EntitySection.new {
            this.title = title
            this.order = min(max, order)
            this.lessonCount = lessonCount
            this.unit = EntityUnit[unitId]
        }
        EntityUnitProgress.find {
            UnitsProgress.unit eq unitId
        }.forEach {
            EntitySectionProgress.new {
                this.unit = it
                this.section = section
            }
        }
        section.id.value
    }
    override fun addWordToSection(wordId: Int, sectionId: Int): Int = transaction {
        EntitySectionWordCrossRef.new {
            this.section = EntitySection[sectionId].id
            this.word = EntityWord[wordId].id
        }.id.value
    }
    override fun removeWordFromSection(wordId: Int, sectionId: Int): Boolean = transaction{
        EntitySectionWordCrossRef.find {
            (SectionWordCrossRefs.section eq sectionId) and (SectionWordCrossRefs.word eq wordId)
        }.firstOrNull()?.let {word->
            word.run {
                delete()
            }
            EntitySectionProgressLearnedWordCrossRef.find {
                (SectionProgressLearnedWordCrossRefs.learnedWord eq wordId)
            }.filter {
                EntitySectionProgress[it.section].section.id.value == sectionId
            }.forEach {
                it.run{
                    delete()
                }
            }
            return@transaction true
        }
        return@transaction false
    }
    override fun addSentenceToSection(sentenceId: Int, sectionId: Int): Int = transaction {
        EntitySectionSentenceCrossRef.new {
            this.section = EntitySection[sectionId].id
            this.sentence = EntitySentence[sentenceId].id
        }.id.value
    }
    override fun removeSentenceFromSection(sentenceId: Int, sectionId: Int): Boolean = transaction {
        EntitySectionSentenceCrossRef.find {
            (SectionSentenceCrossRefs.section eq sectionId) and (SectionSentenceCrossRefs.sentence eq sentenceId)
        }.firstOrNull()
            ?.let {
                it.run {
                    delete()
                }
                return@transaction true
            }
        return@transaction false
    }
    override fun addWord(
        phonic: String,
        sound: String,
        translatedSound: String,
        translatedWord: String,
        word: String,
        languageId: Int
    ): Int = transaction {
        EntityWord.new {
            this.phonic = phonic
            this.sound = sound
            this.translatedWord = translatedWord
            this.translatedSound = translatedSound
            this.word = word
            this.language = EntityLanguage[languageId]
        }.id.value
    }
    override fun addFlag(flagImg: String, flagId: String): String  = transaction{
        EntityFlag.new(flagId){
            this.flag = flagImg
        }.id.value
    }
    override fun getAllFlags(): List<Flag> = transaction{
        EntityFlag.all().map {
            Flag.fromEntity(it)
        }
    }

    override fun getFlagById(flagId: String): Flag? = transaction{
        EntityFlag.findById(flagId)?.let {Flag.fromEntity(it)}
    }

    override fun updateFlag(flagId: String, flagImg: String): String =  transaction {
        EntityFlag[flagId].apply {
            this.flag = flagImg
        }.id.value
    }

    override fun deleteFlag(flagId: String): Boolean = transaction {
        EntityFlag.findById(flagId)?.let {
            it.run {
                delete()
                return@transaction true
            }
        }
        return@transaction false
    }


    override fun addSentence(languageId: Int, words: List<Int>): Int = transaction {
        val sentenceId = EntitySentence.new {
            this.language = EntityLanguage[languageId]
        }.id.value
        words.mapIndexed { order, wordId ->
            EntitySentenceWordCrossRef.new {
                this.word = EntityID(wordId, Words)
                this.order = order
                this.sentence = EntityID(sentenceId, Sentences)
            }
        }
        return@transaction sentenceId
    }

    override fun getLanguage(nativeId: String, languageId: String, depth: Int): Language? = transaction {
        EntityLanguage.find {
            (Languages.nativeId eq nativeId) and (Languages.languageId eq languageId)
        }
            .firstOrNull()
            ?.let { Language.fromEntity(it, depth) }
    }

    override fun getLanguages(nativeId: String, depth: Int): List<Language> = transaction{
        EntityLanguage.find {
            (Languages.nativeId eq nativeId)
        }.map {
            Language.fromEntity(it, depth)
        }
    }

    override fun getLanguageById(languageId: Int, depth: Int): Language? = transaction {
        EntityLanguage.findById(languageId)?.let{ Language.fromEntity(it, depth) }
    }

    override fun getUnit(nativeId: String, languageId: String, unitNum: Int, depth: Int): Unit? = transaction {
        EntityLanguage.find {
            (Languages.nativeId eq nativeId) and (Languages.languageId eq languageId)
        }.firstOrNull()?.let {
            EntityUnit.find {
                (Units.language eq it.id)
            }.firstOrNull()
        }?.let { Unit.fromEntity(it, depth) }
    }

    override fun getUnitById(unitId: Int, depth: Int): Unit? = transaction {
        EntityUnit.findById(unitId)?.let { Unit.fromEntity(it, depth) }
    }

    override fun getSection(
        nativeId: String,
        languageId: String,
        unitNum: Int,
        sectionNum: Int,
        depth: Int
    ): Section? = transaction {
        EntityLanguage.find {
            (Languages.nativeId eq nativeId) and (Languages.languageId eq languageId)
        }.firstOrNull()?.let {
            EntityUnit.find {
                (Units.language eq it.id)
            }.firstOrNull()?.let {
                EntitySection.find {
                    (Sections.unit eq it.id)
                }.firstOrNull()
            }
        }?.let { Section.fromEntity(it, depth) }
    }

    override fun getSectionById(sectionId: Int, depth: Int): Section? = transaction {
        EntitySection.findById(sectionId)?.let { Section.fromEntity(it, depth) }
    }

    override fun getWord(languageId: Int, word: String): Word? = transaction {
        EntityWord.find {
            (Words.language eq languageId) and (Words.word eq word)
        }.firstOrNull()?.let { Word.fromEntity(it) }
    }

    override fun getWordById(wordId: Int): Word? = transaction {
        EntityWord.findById(wordId)?.let { Word.fromEntity(it) }
    }

    override fun getSentence(nativeId: String, languageId: String, sentence: String): Sentence? = transaction {
        var entitySentence: EntitySentence? = null
        EntityLanguage.find {
            (Languages.nativeId eq nativeId) and (Languages.languageId eq languageId)
        }.firstOrNull()?.let {
            EntitySentence.find {
                (Sentences.language eq it.id)
            }.forEach {
                if (checkLists(
                        EntitySentenceWordCrossRef.find { (SentenceWordCrossRefs.sentence eq it.id) }.toList()
                            .map { EntityWord[it.word].word }, sentence.split(" ")
                    )
                ) {
                    entitySentence = it
                }
            }
        }?.let { entitySentence?.let { it1 -> Sentence.fromEntity(it1) } }
    }

    override fun getSentenceById(sentenceId: Int): Sentence? = transaction {
        EntitySentence.findById(sentenceId)?.let { Sentence.fromEntity(it) }
    }

    override fun updateSentence(sentenceId: Int, words: List<Int>): Int = transaction {
        EntitySentenceWordCrossRef.find {
            (SentenceWordCrossRefs.sentence eq sentenceId)
        }.forEach {
            it.run {
                delete()
            }
        }
        words.mapIndexed {index, wordId ->
            EntitySentenceWordCrossRef.new {
                this.word = EntityID(wordId, Words)
                this.order = index
                this.sentence = EntityID(sentenceId, Sentences)
            }
        }
        return@transaction sentenceId
    }

    override fun updateWord(wordId: Int, phonic: String, sound: String, translatedSound: String, translateWord: String, text: String): Int = transaction {
        EntityWord[wordId].apply {
            this.phonic = phonic
            this.sound = sound
            this.translatedSound = translatedSound
            this.translatedWord = translateWord
            this.word = text
        }.id.value
    }
    override fun updateSection(sectionId: Int, title: String, order: Int, lessonCount: Int, unitId: Int): Int = transaction {
        val id = EntitySection[sectionId].apply {
            this.title = title
            this.order = min(updateSectionOrder(order, sectionId, this.order), order)
            this.lessonCount = lessonCount
            this.unit = EntityUnit[unitId]
        }.id.value
        EntitySectionProgress.find {
            SectionsProgress.section eq sectionId
        }.forEach {
            it.apply {
                this.currentLesson = min(this.currentLesson, lessonCount)
            }
        }
        id
    }

    override fun updateUnit(unitId: Int, title: String, order: Int): Int = transaction {
        EntityUnit[unitId].apply {
            this.title = title
            this.order = min(updateSectionOrder(order, unitId, this.order), order)
        }.id.value
    }

    override fun updateLanguage(
        languageId: Int,
        native: String,
        language: String,
        languageName: String,
        flag: String
    ): Int = transaction {
        EntityLanguage[languageId].apply {
            this.languageId = language
            this.nativeId = native
            this.languageName = languageName
            this.flag = EntityID(flag, Flags)
        }.id.value
    }

    override fun deleteLanguage(languageId: Int): Boolean = transaction {
        val eLanguage = EntityLanguage.findById(languageId)
        eLanguage?.run {
            EntityLanguageProgress.find {
                LanguagesProgress.language eq languageId
            }.forEach {
                it.run{
                    delete()
                }
            }
            delete()
            return@transaction true
        }
        return@transaction false
    }

    override fun deleteUnit(unitId: Int): Boolean = transaction {
        val eUnit = EntityUnit.find { (Units.id eq unitId) }.firstOrNull()
        eUnit?.run {
            EntityUnitProgress.find {
                UnitsProgress.unit eq unitId
            }.forEach {
                it.run{
                    delete()
                }
            }
            delete()
            return@transaction true
        }
        return@transaction false
    }

    override fun deleteSection(sectionId: Int): Boolean = transaction {
        val eSection = EntitySection.find { (Sections.id eq sectionId) }.firstOrNull()
        eSection?.run {
            EntitySectionProgress.find {
                SectionsProgress.section eq sectionId
            }.forEach {
                it.run{
                    delete()
                }
            }
            delete()
            return@transaction true
        }
        return@transaction false
    }

    override fun deleteWord(wordId: Int): Boolean = transaction {
        val eWord = EntityWord.findById(wordId)

        eWord?.run {
            EntitySentenceWordCrossRef.find {
                SentenceWordCrossRefs.word eq eWord.id
            }.forEach {
                EntitySentence[it.sentence].run {
                    delete()
                }
            }
            delete()
            return@transaction true
        }
        return@transaction false
    }

    override fun deleteSentence(sentenceId: Int): Boolean = transaction {
        val eSentence = EntitySentence.findById(sentenceId)
        eSentence?.run {
            delete()
            return@transaction true
        }
        return@transaction false
    }
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


    override fun wordExists(word: String, language: Int): Boolean = transaction {
        EntityWord.find { (Words.language eq language) and (Words.word eq word) }.firstOrNull() != null
    }

    private val checkLists: (List<String>, List<String>) -> Boolean = { x: List<String>, y: List<String> ->
        if (x == y) {
            true
        } else {
            x.sorted().toList() == y.sorted().toList()
        }

    }

}