package com.openphonics.application.controller

import com.openphonics.application.exception.BadRequestException
import com.openphonics.application.exception.NotFoundException
import com.openphonics.application.request.LanguageRequest
import com.openphonics.application.request.UpdateLanguageRequest
import com.openphonics.application.response.FlagResponse
import com.openphonics.application.response.IntResponse
import com.openphonics.application.response.LanguageResponse
import com.openphonics.data.language.LanguageDao
import com.openphonics.utils.OpenPhonicsRequestValidator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageController @Inject constructor(
    private val languageDao: LanguageDao,
    private val validator: OpenPhonicsRequestValidator
){
    fun all(nativeId: String): LanguageResponse {
        val languages = languageDao.all(nativeId)
        return LanguageResponse.success(languages)
    }
    fun get(id: Int): LanguageResponse {
        return try {
            validator.languageExistsOrThrowException(id)
            val language = languageDao.get(id)!!
            LanguageResponse.success(language)
        } catch (nfe: NotFoundException) {
            LanguageResponse.notFound(nfe.message)
        }
    }
    fun create(request: LanguageRequest): IntResponse {
        with(request) {
            return try {
                validator.validateOrThrowException(request)
                val responseId = languageDao.create(nativeId, languageId, languageName, flag)
                IntResponse.success(responseId)
            } catch (bre: BadRequestException) {
                IntResponse.failed(bre.message)
            }
        }
    }
    fun update(id: Int, request: UpdateLanguageRequest): IntResponse {
        with(request) {
            return try {
                validator.validateOrThrowException(id, request)
                val responseId = languageDao.update(id, nativeId, languageId, languageName, flag)
                IntResponse.success(responseId)
            } catch (bre: BadRequestException) {
                IntResponse.failed(bre.message)
            } catch (nfe: NotFoundException) {
                IntResponse.notFound(nfe.message)
            }
        }
    }
    fun delete(id: Int): IntResponse {
        return try {
            if (languageDao.delete(id)) {
                IntResponse.success(id)
            } else {
                IntResponse.failed("Error Occurred")
            }
        } catch (bre: BadRequestException) {
            IntResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            IntResponse.notFound(nfe.message)
        }
    }
}