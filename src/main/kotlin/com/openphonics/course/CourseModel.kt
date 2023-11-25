package com.openphonics.course

import com.openphonics.language.LanguageEntity
import com.openphonics.language.Languages
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import kotlinx.serialization.Serializable

object Courses : IntIdTable() {
    val sourceLanguage = reference("source_language", Languages)
    val targetLanguage = reference("target_language", Languages)
}

class CourseEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CourseEntity>(Courses)

    var sourceLanguage by LanguageEntity referencedOn Courses.sourceLanguage
    var targetLanguage by LanguageEntity referencedOn Courses.targetLanguage
}

@Serializable
sealed class CourseTemplate {
    abstract val sourceLanguage: Int?
    abstract val targetLanguage: Int?
}

@Serializable
data class CourseCreate(
    override val sourceLanguage: Int,
    override val targetLanguage: Int
) : CourseTemplate()

@Serializable
data class CourseUpdate(
    override val sourceLanguage: Int,
    override val targetLanguage: Int
) : CourseTemplate()

@Serializable
data class CourseBase(
    val id: Int,
    override val sourceLanguage: Int,
    override val targetLanguage: Int
) : CourseTemplate()