package com.openphonics.data.word

import com.openphonics.data.language.LanguageEntity
import com.openphonics.utils.OpenPhonicsMapper
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Inject
import javax.inject.Singleton

interface WordDao {
    fun create(language: Int, phonic: String, sound: String, translatedWord: String, translatedSound: String, word: String): Int
    fun update(
        id: Int,
        phonic: String? = null,
        sound: String? = null,
        translatedWord: String? = null,
        translatedSound: String? = null,
        word: String? = null
    ): Int
    fun get(id: Int): Word?
    fun get(language: Int, word: String): Word?
    fun all(language: Int): List<Word>
    fun delete(id: Int): Boolean
    fun exists(id: Int): Boolean
    fun exists(language: Int, word: String): Boolean
}
@Singleton
class WordDaoImpl @Inject constructor(
    private val mapper: OpenPhonicsMapper
) : WordDao {
    override fun create(language: Int, phonic: String, sound: String, translatedWord: String, translatedSound: String, word: String): Int = transaction {
        WordEntity.new {
            this.phonic = phonic
            this.sound = sound
            this.translatedWord = translatedWord
            this.translatedSound = translatedSound
            this.word = word
            this.language = LanguageEntity[language]
        }.id.value
    }
    override fun update(
        id: Int,
        phonic: String?,
        sound: String?,
        translatedWord: String?,
        translatedSound: String?,
        word: String?
    ): Int = transaction {
        WordEntity[id].apply {
            phonic?.let { this.phonic = it }
            sound?.let { this.sound = it }
            translatedWord?.let { this.translatedWord = it }
            translatedSound?.let { this.translatedSound = it }
            word?.let { this.word = it }
        }.id.value
    }
    override fun get(id: Int): Word? = transaction {
        WordEntity.findById(id)?.let {mapper.fromEntity(it)}
    }
    override fun get(language: Int, word: String): Word? = transaction {
        WordEntity.find {
            ((Words.language eq language) and (Words.word eq word))
        }.firstOrNull()?.let {mapper.fromEntity(it)}
    }

    override fun all(language: Int): List<Word> = transaction {
        WordEntity.find {
            (Words.language eq language)
        }.sortedBy { it.word}.map {mapper.fromEntity(it)}
    }
    override fun delete(id: Int): Boolean = transaction {
        WordEntity.findById(id)?.run {
            delete()
            return@transaction true
        }
        return@transaction false
    }
    override fun exists(id: Int) = get(id) != null
    override fun exists(language: Int, word: String) = get(language, word) != null
}