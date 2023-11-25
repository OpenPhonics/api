package com.openphonics.language

import com.openphonics.common.core.DAO
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Inject
import javax.inject.Singleton


typealias LanguageOperationsDAO = LanguageOperations<LanguageBase?, List<LanguageBase>>
abstract class LanguageDAO(table: IntEntityClass<LanguageEntity>) :
    DAO<LanguageTemplate, LanguageCreate, LanguageUpdate, LanguageBase, LanguageEntity>(table),
    LanguageOperationsDAO

@Singleton
class LanguageDAOImpl @Inject constructor() : LanguageDAO(LanguageEntity) {
    override fun create(data: LanguageCreate): Int = transaction {
        table.new {
            this.languageCode = data.languageCode
            this.languageName = data.languageName
            this.countryCode = data.countryCode
        }.id.value
    }

    override fun update(id: Int, data: LanguageUpdate): Int = transaction {
        table[id].apply {
            this.languageCode = data.languageCode
            this.languageName = data.languageName
            this.countryCode = data.countryCode
        }.id.value
    }

    override fun convert(entity: LanguageEntity): LanguageBase = LanguageBase(
        entity.id.value,
        entity.languageCode,
        entity.languageName,
        entity.countryCode
    )

    override fun getByLanguageCode(code: String): LanguageBase? = transaction {
        table.find {
            Languages.languageCode eq code
        }.firstOrNull()?.let { convert(it) }
    }

    override fun getByLanguageName(name: String): LanguageBase? = transaction {
        table.find {
            Languages.languageName eq name
        }.firstOrNull()?.let { convert(it) }
    }

    override fun all(): List<LanguageBase> = transaction {
        table.all().map { convert(it) }
    }
}