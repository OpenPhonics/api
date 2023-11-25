package com.openphonics.language

import com.openphonics.common.core.Controller
import com.openphonics.common.core.DataResponse
import com.openphonics.common.exception.BadRequestException
import com.openphonics.common.exception.NotFoundException
import com.openphonics.common.ext.containsOnlyLetters
import javax.inject.Inject
import javax.inject.Singleton

typealias LanguageOperationsController = LanguageOperations<DataResponse<LanguageBase>, DataResponse<LanguageBase>>
abstract class LanguageController(dao: LanguageDAO) :
    Controller<LanguageTemplate, LanguageCreate, LanguageUpdate, LanguageBase, LanguageEntity>(dao),
    LanguageOperationsController

@Singleton
class LanguageControllerImpl @Inject constructor(
    override val dao: LanguageDAO
) : LanguageController(dao) {

    override fun validateOrThrowException(id: Int, request: LanguageUpdate) {
        existsOrThrowException(id)
        with(request){
            val message = when {
                languageCode.length != 2 -> "language code must be 2 characters"
                !languageCode.containsOnlyLetters() -> "language code cannot contain numbers"
                countryCode.length != 2 -> "country code must be 2 characters"
                !countryCode.containsOnlyLetters() -> "country code cannot contain numbers"
                languageName.length > 30 -> "language name must be less then 30 characters"
                !languageName.containsOnlyLetters() -> "language name cannot contain numbers"
                dao.getByLanguageCode(languageCode).let {it != null && it.id != id} -> "language code already exists"
                dao.getByLanguageName(languageName).let {it != null && it.id != id} -> "language name already exists"
                else -> return
            }
            throw BadRequestException(message)
        }
    }

    override fun validateOrThrowException(request: LanguageCreate) {
        with(request){
            val message = when {
                languageCode.length != 2 -> "language code must be 2 characters"
                !languageCode.containsOnlyLetters() -> "language code cannot contain numbers"
                countryCode.length != 2 -> "country code must be 2 characters"
                !countryCode.containsOnlyLetters() -> "country code cannot contain numbers"
                languageName.length > 30 -> "language name must be less then 30 characters"
                !languageName.containsOnlyLetters() -> "language name cannot contain numbers"
                dao.getByLanguageCode(languageCode) != null -> "language code already exists"
                dao.getByLanguageName(languageName) != null -> "language name already exists"
                else -> return
            }
            throw BadRequestException(message)
        }
    }

    override fun getByLanguageCode(code: String): DataResponse<LanguageBase> {
        return try {
            if (dao.getByLanguageCode(code) == null) {
                throw NotFoundException("language code $code does not exist")
            }
            val data = dao.getByLanguageCode(code)!!
            DataResponse.success(data)
        } catch (nfe: NotFoundException) {
            DataResponse.notFound(nfe.message)
        }
    }

    override fun getByLanguageName(name: String): DataResponse<LanguageBase> {
        return try {
            if (dao.getByLanguageName(name) == null) {
                throw NotFoundException("language name $name does not exist")
            }
            val data = dao.getByLanguageName(name)!!
            DataResponse.success(data)
        } catch (nfe: NotFoundException) {
            DataResponse.notFound(nfe.message)
        }
    }

    override fun all(): DataResponse<LanguageBase> {
        val data = dao.all()
        return DataResponse.success(data)
    }
}