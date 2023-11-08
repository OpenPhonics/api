package com.openphonics.application.controller

import com.openphonics.application.exception.BadRequestException
import com.openphonics.application.exception.NotFoundException
import com.openphonics.application.request.UpdateWordRequest
import com.openphonics.application.request.WordRequest
import com.openphonics.application.response.FlagResponse
import com.openphonics.application.response.IntResponse
import com.openphonics.application.response.WordResponse
import com.openphonics.data.word.WordDao
import com.openphonics.utils.OpenPhonicsRequestValidator
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WordController @Inject constructor(
    private val wordDao: WordDao,
    private val validator: OpenPhonicsRequestValidator
) {
    fun all(language: Int): WordResponse {
        return try {
            validator.languageExistsOrThrowException(language)
            val words = wordDao.all(language)
            WordResponse.success(words)
        } catch (nfe: NotFoundException) {
            WordResponse.notFound(nfe.message)
        }
    }
    fun get(id: Int): WordResponse {
        return try {
            validator.wordExistsOrThrowException(id)
            val word = wordDao.get(id)!!
            WordResponse.success(word)
        } catch (nfe: NotFoundException) {
            WordResponse.notFound(nfe.message)
        }
    }
    fun create(request: WordRequest): IntResponse {
        with(request) {
            return try {
                validator.validateOrThrowException(request)
                val responseId = wordDao.create(language, phonic, sound, translatedWord, translatedSound, word)
                IntResponse.success(responseId)
            } catch (bre: BadRequestException) {
                IntResponse.failed(bre.message)
            }
        }
    }
    fun update(id: Int, request: UpdateWordRequest): IntResponse {
        with(request) {
            return try {
                validator.validateOrThrowException(id, request)
                val responseId = wordDao.update(id, phonic, sound, translatedWord, translatedSound, word)
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
            if (wordDao.delete(id)) {
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