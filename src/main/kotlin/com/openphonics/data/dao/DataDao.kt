package com.openphonics.data.dao
import com.openphonics.data.database.table.data.*
import com.openphonics.data.database.table.references.SectionSentenceCrossRefs
import com.openphonics.data.database.table.references.SectionWordCrossRefs
import com.openphonics.data.database.table.references.SentenceWordCrossRefs
import com.openphonics.data.entity.data.*
import com.openphonics.data.entity.references.EntitySectionSentenceCrossRef
import com.openphonics.data.entity.references.EntitySectionWordCrossRef
import com.openphonics.data.entity.references.EntitySentenceWordCrossRef
import com.openphonics.data.model.data.*
import com.openphonics.data.model.data.Unit
import org.jetbrains.exposed.dao.id.EntityID
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
    fun addSentenceToSection(sentenceId: Int, sectionId: Int): Int
    fun addWordToSentence(sentenceId: Int, wordId: Int): Int
    fun addWord(
        phonic: String,
        sound: String,
        translatedSound: String,
        translatedWord: String,
        word: String,
        languageId: Int
    ): Int

    fun addSentence(languageId: Int, words: List<String>): Int
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
    fun getWord(nativeId: String, languageId: String, word: String): Word?
    fun getWordById(wordId: Int): Word?
    fun getSentence(nativeId: String, languageId: String, sentence: String): Sentence?
    fun getSentenceById(sentenceId: Int): Sentence?

    fun updateUnit(unitId: Int, title: String, order: Int, languageId: Int): Int

    fun updateLanguage(languageId: Int, native: String, language: String, languageName: String, flag: String): Int

//    fun getUnitsByLanguage(languageId: Int): List<Unit>
//    fun getSectionsByUnit(unitId: Int): List<Section>
//    fun getAllWordsByLanguage(languageId: Int): List<Word>
//    fun getAllSentencesByLanguage(languageId: Int): List<Sentence>
//    fun getSectionSentenceReferences(sectionId: Int): List<SectionSentenceCrossRef>
//    fun getSectionWordReferences(sectionId: Int): List<SectionWordCrossRef>
//    fun getSentenceWordReferences(sentenceId: Int): List<SentenceWordCrossRef>

    fun exists(id: Int, type: Any): Boolean

    fun nativeExists(native: String): Boolean

    fun flagExists(flag: String): Boolean

    fun wordExists(word: String, language: Int): Boolean
}



@Singleton
class DataDaoImpl @Inject constructor() : DataDao {
    override fun addLanguage(
        nativeId: String,
        languageId: String,
        languageName: String,
        flag: String
    ): Int = transaction {
        EntityLanguage.new {
            this.nativeId = nativeId
            this.languageId = languageId
            this.languageName = languageName
            this.flag = EntityID(flag, Flags)
        }.id.value
    }

    private fun updateUnitOrder(order: Int, languageId: Int): Int = transaction {
        var max = 0
        EntityUnit.find {
            (Units.language eq languageId)
        }.map {
            if (it.order >= order) {
                it.apply {
                    this.order += 1
                }
            }
            if (it.order > max) max = it.order
        }
        max + 1
    }
    private fun updateSectionOrder(order: Int, unitId: Int): Int = transaction{
        var max = 0
        EntitySection.find {
            (Sections.unit eq unitId)
        }.map {
            if (it.order >= order) {
                it.apply {
                    this.order += 1
                }
            }
            if (it.order > max) max = it.order
        }
        max +1
    }

    override fun addUnit(
        title: String,
        order: Int,
        languageId: Int
    ): Int = transaction {
        val max = updateUnitOrder(order, languageId)
        EntityUnit.new {
            this.title = title
            this.order = min(max, order)
            this.language = EntityLanguage[languageId]
        }.id.value

    }
    override fun addSection(
        title: String,
        order: Int,
        lessonCount: Int,
        unitId: Int
    ): Int = transaction {
        val max = updateSectionOrder(order, unitId)
        EntitySection.new {
            this.title = title
            this.order = min(max, order)
            this.lessonCount = lessonCount
            this.unit = EntityUnit[unitId]
        }.id.value
    }
    override fun addWordToSection(wordId: Int, sectionId: Int): Int = transaction {
        EntitySectionWordCrossRef.new {
            this.section = EntitySection[sectionId].id
            this.word = EntityWord[wordId].id
        }.id.value
    }
    override fun addSentenceToSection(sentenceId: Int, sectionId: Int): Int = transaction {
        EntitySectionSentenceCrossRef.new {
            this.section = EntitySection[sectionId].id
            this.sentence = EntitySentence[sentenceId].id
        }.id.value
    }
    override fun addWordToSentence(sentenceId: Int, wordId: Int): Int = transaction {
        EntitySentenceWordCrossRef.new {
            this.sentence = EntitySentence[sentenceId].id
            this.word = EntityWord[wordId].id
        }.id.value
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
    override fun addSentence(languageId: Int, words: List<String>): Int = transaction {
        val sentenceId = EntitySentence.new {
            this.language = EntityLanguage[languageId]
        }.id.value
        words.map { word ->
            EntityWord.find { (Words.language eq languageId) and (Words.word eq word) }.firstOrNull()
        }.mapNotNull { word ->
            addWordToSentence(sentenceId, word!!.id.value)
        }
        return@transaction sentenceId
    }

    override fun getLanguage(nativeId: String, languageId: String, depth: Int): Language? = transaction {
        EntityLanguage.find {
            (Languages.nativeId eq nativeId) and (Languages.languageId eq languageId)
        }.firstOrNull()
    }?.let { Language.fromEntity(it, depth) }

    override fun getLanguages(nativeId: String, depth: Int): List<Language> = transaction{
        EntityLanguage.find {
            (Languages.nativeId eq nativeId)
        }.map {
            Language.fromEntity(it, depth)
        }
    }

    override fun getLanguageById(languageId: Int, depth: Int): Language? = transaction {
        EntityLanguage.findById(languageId)
    }?.let { Language.fromEntity(it, depth) }

    override fun getUnit(nativeId: String, languageId: String, unitNum: Int, depth: Int): Unit? = transaction {
        EntityLanguage.find {
            (Languages.nativeId eq nativeId) and (Languages.languageId eq languageId)
        }.firstOrNull()?.let {
            EntityUnit.find {
                (Units.language eq it.id)
            }.firstOrNull()
        }
    }?.let { Unit.fromEntity(it, depth) }

    override fun getUnitById(unitId: Int, depth: Int): Unit? = transaction {
        EntityUnit.findById(unitId)
    }?.let { Unit.fromEntity(it, depth) }

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
        }
    }?.let { Section.fromEntity(it, depth) }

    override fun getSectionById(sectionId: Int, depth: Int): Section? = transaction {
        EntitySection.findById(sectionId)
    }?.let { Section.fromEntity(it, depth) }

    override fun getWord(nativeId: String, languageId: String, word: String): Word? = transaction {
        EntityLanguage.find {
            (Languages.nativeId eq nativeId) and (Languages.languageId eq languageId)
        }.firstOrNull()?.let {
            EntityWord.find {
                (Words.language eq it.id) and (Words.word eq word)
            }.firstOrNull()
        }
    }?.let { Word.fromEntity(it) }

    override fun getWordById(wordId: Int): Word? = transaction {
        EntityWord.findById(wordId)
    }?.let { Word.fromEntity(it) }

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
        EntitySentence.findById(sentenceId)
    }?.let { Sentence.fromEntity(it) }

    override fun updateUnit(unitId: Int, title: String, order: Int, languageId: Int): Int = transaction {
        val max = updateUnitOrder(order, languageId)
        EntityUnit[unitId].apply {
            this.title = title
            this.order = min(max, order)
            this.language = EntityLanguage[languageId]
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
            eLanguage.units.map { unit ->
                deleteUnit(unit.id.value)
            }
            EntityWord.find { Words.language eq eLanguage.id }.forEach { word ->
                word.run {
                    delete()
                }
            }
            EntitySentence.find { Sentences.language eq eLanguage.id }.forEach { sentence ->
                sentence.run {
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
            eUnit.sections.map { section ->
                deleteSection(section.id.value)
            }
            delete()
            return@transaction true
        }
        return@transaction false
    }

    override fun deleteSection(sectionId: Int): Boolean = transaction {
        val eSection = EntitySection.find { (Sections.id eq sectionId) }.firstOrNull()
        eSection?.run {
            EntitySectionWordCrossRef.find { SectionWordCrossRefs.section eq eSection.id }
                .forEach { ref ->
                    ref.run {
                        delete()
                    }
                }
            EntitySectionSentenceCrossRef.find { SectionSentenceCrossRefs.section eq eSection.id }
                .forEach { ref ->
                    ref.run {
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
        when (type::class) {
            EntityLanguage::class -> EntityLanguage
            EntityUnit::class -> EntitySection
            EntitySection::class -> EntityUnit
            EntitySentence::class -> EntitySentence
            EntityWord::class -> EntityWord
            EntitySectionSentenceCrossRef::class -> EntitySectionSentenceCrossRef
            EntitySectionWordCrossRef::class -> EntitySectionWordCrossRef
            EntitySentenceWordCrossRef::class -> EntitySentenceWordCrossRef
            else -> throw IllegalArgumentException("Unsupported data type: ${type::class.simpleName}")
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