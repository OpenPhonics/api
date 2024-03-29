package com.openphonics.courseword

import com.openphonics.common.core.Controller
import com.openphonics.common.core.DataResponse
import com.openphonics.common.exception.BadRequestException
import com.openphonics.common.exception.NotFoundException
import com.openphonics.course.CourseDAO
import com.openphonics.course.CourseEntity
import com.openphonics.language.*
import com.openphonics.word.WordDAO
import com.openphonics.word.WordEntity
import com.openphonics.word.Words
import javax.inject.Inject
import javax.inject.Singleton

typealias CourseWordOperationsController = CourseWordOperations<DataResponse<List<CourseWordBase>>>
abstract class CourseWordController(dao: CourseWordDAO) :
    Controller<CourseWordTemplate, CourseWordCreate, CourseWordUpdate, CourseWordBase, CourseWordEntity>(dao),
    CourseWordOperationsController

@Singleton
class CourseWordControllerImpl @Inject constructor(
    override val dao: CourseWordDAO,
    private val wordDao: WordDAO,
    private val courseDao: CourseDAO
) : CourseWordController(dao) {

    override fun validateOrThrowException(id: Int, request: CourseWordUpdate) {
        existsOrThrowException(id)
        with(request){
            existsWordIdOrThrowException(sourceWord)
            existsWordIdOrThrowException(targetWord)
        }
    }

    override fun validateOrThrowException(request: CourseWordCreate) {
        with(request){
            existsCourseIdOrThrowException(course)
            existsWordIdOrThrowException(sourceWord)
            existsWordIdOrThrowException(targetWord)
        }
    }
    private fun existsWordIdOrThrowException(id: Int){
        if (wordDao.get(id) == null)
            throw BadRequestException("id code does not exist")
    }
    private fun existsCourseIdOrThrowException(id: Int){
        if (courseDao.get(id) == null)
            throw BadRequestException("id code does not exist")
    }
    override fun all(course: Int): DataResponse<List<CourseWordBase>> {
        return try {
            existsCourseIdOrThrowException(course)
            val data = dao.all(course)
            DataResponse.success(data)
        } catch (bre: BadRequestException) {
            DataResponse.failed(bre.message)
        }
    }
}