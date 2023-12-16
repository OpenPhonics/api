package com.openphonics.courseword

import com.openphonics.course.CourseEntity
import com.openphonics.course.Courses
import com.openphonics.word.WordEntity
import com.openphonics.word.Words
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import kotlinx.serialization.Serializable

object CourseWords : IntIdTable() {
    val sourceWord = reference("source_word", Words)
    val targetWord = reference("target_word", Words)
    val course = reference("course", Courses)
}

class CourseWordEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CourseWordEntity>(CourseWords)

    var sourceWord by WordEntity referencedOn CourseWords.sourceWord
    var targetWord by WordEntity referencedOn CourseWords.targetWord
    var course by CourseEntity referencedOn  CourseWords.course
}

@Serializable
sealed class CourseWordTemplate {
    abstract val sourceWord: Int?
    abstract val targetWord: Int?
    abstract val course: Int?
}

@Serializable
data class CourseWordCreate(
    override val sourceWord: Int,
    override val targetWord: Int,
    override val course: Int
) : CourseWordTemplate()

@Serializable
data class CourseWordUpdate(
    override val sourceWord: Int,
    override val targetWord: Int
) : CourseWordTemplate() {
    override val course: Int? = null
}

@Serializable
data class CourseWordBase(
    val id: Int,
    override val sourceWord: Int,
    override val targetWord: Int,
    override val course: Int
) : CourseWordTemplate()