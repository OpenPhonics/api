package com.openphonics.word

import com.openphonics.common.core.Controller
import com.openphonics.common.core.DataResponse
import com.openphonics.common.exception.BadRequestException
import com.openphonics.common.exception.NotFoundException
import com.openphonics.common.utils.ext.containsOnlyLetters
import com.openphonics.language.LanguageBase
import com.openphonics.language.LanguageDAO
import com.openphonics.language.LanguageEntity
import com.openphonics.language.LanguageOperations
import javax.inject.Inject
import javax.inject.Singleton
typealias WordOperationsController = WordOperations<DataResponse<WordBase>, DataResponse<WordBase>>

abstract class WordController(dao: WordDAO) :
    Controller<WordTemplate, WordCreate, WordUpdate, WordBase, WordEntity>(dao),
    WordOperationsController
@Singleton
class WordControllerImpl @Inject constructor(
    override val dao: WordDAO,
    private val languageDao: LanguageDAO
) : WordController(dao) {

    override fun validateOrThrowException(id: Int, request: WordUpdate) {
        val entity = existsOrThrowException(id)
        with(request){
            val message = when {
                word.length > 100 -> "word must be less than 100 characters"
                !word.containsOnlyLetters() -> "word cannot contain numbers"
                phonic != null && phonic.length > 100 -> "phonic must be less than 100 characters"
                phonic != null && !phonic.containsOnlyLetters() -> "phonic cannot contain numbers"
                dao.get(word, entity.language) != null -> "word already exists"
                else -> return
            }
            throw BadRequestException(message)
        }
    }

    override fun validateOrThrowException(request: WordCreate) {
        with(request){
            if (languageDao.get(language) == null)
                throw BadRequestException("language does not exist")
            val message = when {
                word.length > 100 -> "word must be less than 100 characters"
                !word.containsOnlyLetters() -> "word cannot contain numbers"
                phonic != null && phonic.length > 100 -> "phonic must be less than 100 characters"
                phonic != null && !phonic.containsOnlyLetters() -> "phonic cannot contain numbers"
                dao.get(word, language) != null -> "word already exists"
                else -> return
            }
            throw BadRequestException(message)
        }
    }

    override fun get(word: String, language: Int): DataResponse<WordBase> {
        return try {
            val data = dao.get(word, language) ?: throw NotFoundException("word $word does not exist")
            DataResponse.success(data)
        } catch (nfe: NotFoundException) {
            DataResponse.notFound(nfe.message)
        }
    }

    override fun all(language: Int): DataResponse<WordBase> {
        return try {
        if (languageDao.get(language) == null){
            throw BadRequestException("language does not exist")
        }
        val data = dao.all(language)
        DataResponse.success(data)
        } catch (bre: BadRequestException) {
            DataResponse.failed(bre.message)
        }
    }
}