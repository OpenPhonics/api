package com.openphonics.word

import com.openphonics.language.LanguageEntity
import com.openphonics.language.Languages
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Words : IntIdTable() {
    val word = varchar("word", length = 100)
    val phonic = varchar("phonic", length = 100).nullable()
    val language = reference("language", Languages)
    init {
        uniqueIndex(word, language)
    }
}

class WordEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<WordEntity>(Words)

    var word by Words.word
    var phonic by Words.phonic
    var language by LanguageEntity referencedOn Words.language
}

@Serializable
sealed class WordTemplate {
    abstract val word: String?
    abstract val phonic: String?
    abstract val language: Int?
}

@Serializable
data class WordCreate(
    override val word: String,
    override val phonic: String?,
    override val language: Int
) : WordTemplate()

@Serializable
data class WordUpdate(
    override val word: String,
    override val phonic: String?
) : WordTemplate(){
    override val language: Int? = null
}

@Serializable
data class WordBase(
    val id: Int,
    override val word: String,
    override val phonic: String?,
    override val language: Int
) : WordTemplate()

