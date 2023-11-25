package com.openphonics.word

import com.openphonics.common.core.DAO
import com.openphonics.language.LanguageBase
import com.openphonics.language.LanguageEntity
import com.openphonics.language.LanguageOperations
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Inject
import javax.inject.Singleton

typealias WordOperationsDAO = WordOperations<WordBase?, List<WordBase>>

abstract class WordDAO(table: IntEntityClass<WordEntity>) :
    DAO<WordTemplate, WordCreate, WordUpdate, WordBase, WordEntity>(table),
    WordOperationsDAO

@Singleton
class WordDAOImpl @Inject constructor() : WordDAO(WordEntity) {
    override fun create(data: WordCreate): Int = transaction {
        table.new {
            this.word = data.word
            this.phonic = data.phonic
            this.language = LanguageEntity[data.language]
        }.id.value
    }

    override fun update(id: Int, data: WordUpdate): Int = transaction {
        table[id].apply {
            this.word = data.word
            this.phonic = data.phonic
        }.id.value
    }

    override fun convert(entity: WordEntity): WordBase = WordBase(
        entity.id.value,
        entity.word,
        entity.phonic,
        entity.language.id.value
    )

    override fun get(word: String, language: Int): WordBase? = transaction {
        table.find {
            (Words.language eq language) and (Words.word eq word)
        }.firstOrNull()?.let { convert(it) }
    }

    override fun all(language: Int): List<WordBase> = transaction {
        table.find {
            Words.language eq language
        }.map { convert(it) }
    }
}