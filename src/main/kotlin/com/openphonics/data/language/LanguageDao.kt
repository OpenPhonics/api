package com.openphonics.data.language

import com.openphonics.data.flag.FlagEntity
import com.openphonics.utils.OpenPhonicsMapper
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Inject
import javax.inject.Singleton

interface LanguageDao {
    fun create(nativeId: String, languageId: String, languageName: String, flag: String): Int
    fun all(nativeId: String): List<Language>
    fun update(id: Int, nativeId: String? = null, languageId: String? = null, languageName: String? = null, flag: String? = null): Int
    fun get(id: Int): Language?
    fun get(nativeId: String, languageId: String): Language?
    fun delete(id: Int): Boolean
    fun exists(id: Int): Boolean
    fun exists(nativeId: String, languageId: String): Boolean
}
@Singleton
class LanguageDaoImpl @Inject constructor(
    private val mapper: OpenPhonicsMapper
) : LanguageDao {
    override fun create(nativeId: String, languageId: String, languageName: String, flag: String): Int = transaction {
        LanguageEntity.new {
            this.nativeId = nativeId
            this.languageId = languageId
            this.languageName = languageName
            this.flag = FlagEntity[flag]
        }.id.value
    }
    override fun all(nativeId: String): List<Language> = transaction {
        LanguageEntity.find {
            (Languages.nativeId eq nativeId)
        }.map {mapper.fromEntity(it)}
    }
    override fun update(id: Int, nativeId: String?, languageId: String?, languageName: String?, flag: String?): Int = transaction {
        LanguageEntity[id].apply {
            nativeId?.let { this.nativeId = it }
            languageId?.let { this.languageId = it }
            languageName?.let { this.languageName = it }
            flag?.let { this.flag = FlagEntity[flag] }
        }.id.value
    }
    override fun get(id: Int): Language? = transaction {
        LanguageEntity.findById(id)?.let {mapper.fromEntity(it)}
    }
    override fun get(nativeId: String, languageId: String): Language? = transaction {
        LanguageEntity.find {
            ((Languages.nativeId eq nativeId) and (Languages.languageId eq languageId))
        }.firstOrNull()?.let {mapper.fromEntity(it)}
    }
    override fun delete(id: Int): Boolean = transaction {
        LanguageEntity.findById(id)?.run {
            delete()
            return@transaction true
        }
        return@transaction false
    }
    override fun exists(id: Int) = get(id) != null
    override fun exists(nativeId: String, languageId: String) = get(nativeId, languageId) != null
}