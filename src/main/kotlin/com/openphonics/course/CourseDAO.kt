package com.openphonics.course

import com.openphonics.common.core.DAO
import com.openphonics.language.*
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Inject
import javax.inject.Singleton

typealias CourseOperationsDAO = CourseOperations<List<CourseBase>>
abstract class CourseDAO(table: IntEntityClass<CourseEntity>) :
    DAO<CourseTemplate, CourseCreate, CourseUpdate, CourseBase, CourseEntity>(table),
    CourseOperationsDAO

@Singleton
class CourseDAOImpl @Inject constructor() : CourseDAO(CourseEntity) {
    override fun create(data: CourseCreate): Int = transaction {
        table.new {
            this.sourceLanguage = LanguageEntity[data.sourceLanguage]
            this.targetLanguage = LanguageEntity[data.targetLanguage]
        }.id.value
    }

    override fun update(id: Int, data: CourseUpdate): Int = transaction {
        table[id].apply {
            this.sourceLanguage = LanguageEntity[data.sourceLanguage]
            this.targetLanguage = LanguageEntity[data.targetLanguage]
        }.id.value
    }

    override fun convert(entity: CourseEntity): CourseBase = CourseBase(
        entity.id.value,
        entity.sourceLanguage.id.value,
        entity.targetLanguage.id.value
    )

    override fun all(languageCode: String): List<CourseBase> = transaction {
        val id = LanguageEntity.find {
            Languages.languageCode eq languageCode
        }.firstOrNull()?.id?.value!!
        table.find {
            Courses.sourceLanguage eq id
        }.map { convert(it) }
    }
}