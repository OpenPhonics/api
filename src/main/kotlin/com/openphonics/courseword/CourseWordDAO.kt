package com.openphonics.courseword

import com.openphonics.common.core.DAO
import com.openphonics.course.*
import com.openphonics.language.*
import com.openphonics.word.WordEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Inject
import javax.inject.Singleton

typealias CourseWordOperationsDAO = CourseWordOperations<List<CourseWordBase>>
abstract class CourseWordDAO(table: IntEntityClass<CourseWordEntity>) :
    DAO<CourseWordTemplate, CourseWordCreate, CourseWordUpdate, CourseWordBase, CourseWordEntity>(table),
    CourseWordOperationsDAO

@Singleton
class CourseWordDAOImpl @Inject constructor() : CourseWordDAO(CourseWordEntity) {
    override fun create(data: CourseWordCreate): Int = transaction {
        table.new {
            this.sourceWord = WordEntity[data.sourceWord]
            this.targetWord = WordEntity[data.targetWord]
        }.id.value
    }

    override fun update(id: Int, data: CourseWordUpdate): Int = transaction {
        table[id].apply {
            this.sourceWord = WordEntity[data.sourceWord]
            this.targetWord = WordEntity[data.targetWord]
        }.id.value
    }

    override fun convert(entity: CourseWordEntity): CourseWordBase = CourseWordBase(
        entity.id.value,
        entity.sourceWord.id.value,
        entity.targetWord.id.value,
        entity.course.id.value
    )

    override fun all(course: Int): List<CourseWordBase> = transaction {
        table.find {
            CourseWords.course eq course
        }.map { convert(it) }
    }
}