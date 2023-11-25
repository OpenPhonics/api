package com.openphonics.course

import com.openphonics.common.core.Controller
import com.openphonics.common.core.DataResponse
import com.openphonics.common.exception.BadRequestException
import com.openphonics.language.*
import javax.inject.Inject
import javax.inject.Singleton

typealias CourseOperationsController = CourseOperations<DataResponse<CourseBase>>
abstract class CourseController(dao: CourseDAO) :
    Controller<CourseTemplate, CourseCreate, CourseUpdate, CourseBase, CourseEntity>(dao),
    CourseOperationsController

@Singleton
class CourseControllerImpl @Inject constructor(
    override val dao: CourseDAO
) : CourseController(dao) {

    override fun validateOrThrowException(id: Int, request: CourseUpdate) {
        existsOrThrowException(id)
        with(request){
            existsLanguageIdOrThrowException(sourceLanguage)
            existsLanguageIdOrThrowException(targetLanguage)
        }
    }

    override fun validateOrThrowException(request: CourseCreate) {
        with(request){
            existsLanguageIdOrThrowException(sourceLanguage)
            existsLanguageIdOrThrowException(targetLanguage)
        }
    }
    private fun existsLanguageCodeOrThrowException(languageCode: String){
        if (LanguageEntity.find {
                Languages.languageCode eq languageCode
            }.firstOrNull() == null)
            throw BadRequestException("language code does not exist")
    }
    private fun existsLanguageIdOrThrowException(id: Int){
        if (LanguageEntity.findById(id) == null)
            throw BadRequestException("id code does not exist")
    }
    override fun all(languageCode: String): DataResponse<CourseBase> {
        return try {
            existsLanguageCodeOrThrowException(languageCode)
            val data = dao.all(languageCode)
            DataResponse.success(data)
        } catch (bre: BadRequestException) {
            DataResponse.failed(bre.message)
        }
    }
}