package com.openphonics.controller

import com.openphonics.exception.BadRequestException
import com.openphonics.exception.NotFoundException
import com.openphonics.exception.UnauthorizedActivityException
import com.openphonics.model.request.*
import com.openphonics.model.response.*
import com.openphonics.model.response.Unit
import com.openphonics.utils.isNumeric
import com.openphonics.data.dao.DataDaoImpl
import com.openphonics.data.entity.data.EntityLanguage
import com.openphonics.data.entity.data.EntitySection
import com.openphonics.data.entity.data.EntityUnit
import com.openphonics.data.entity.progress.EntityLanguageProgress
import com.openphonics.data.entity.references.EntitySectionProgressLearnedWordCrossRef
import com.openphonics.data.model.user.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataController @Inject constructor(
    private val dataDao: DataDaoImpl
) {
    fun getLanguage(languageId: Int, depth: DepthRequest): LanguageResponse {
        return try {
            validateDepthOrThrowException(depth.depth)
            checkDataExistsOrThrowException(languageId, EntityLanguage)
            val language = dataDao.getLanguageById(languageId, depth.depth)!!
            LanguageResponse.success(Language.create(language))
        } catch (bre: BadRequestException) {
            LanguageResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            LanguageResponse.unauthorized(uae.message)
        } catch (nfe: NotFoundException) {
            LanguageResponse.notFound(nfe.message)
        }
    }
    fun getSection(sectionId: Int, depth: DepthRequest): SectionResponse {
        return try {
            validateDepthOrThrowException(depth.depth)
            checkDataExistsOrThrowException(sectionId, EntitySection)
            val section = dataDao.getSectionById(sectionId, depth.depth)!!
            SectionResponse.success(Section.create(section))
        } catch (bre: BadRequestException) {
            SectionResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            SectionResponse.unauthorized(uae.message)
        } catch (nfe: NotFoundException) {
            SectionResponse.notFound(nfe.message)
        }
    }
    fun getUnit(unitId: Int, depth: DepthRequest): UnitResponse {
        return try {
            validateDepthOrThrowException(depth.depth)
            checkDataExistsOrThrowException(unitId, EntityUnit)
            val unit = dataDao.getUnitById(unitId, depth.depth)!!
            UnitResponse.success(Unit.create(unit))
        } catch (bre: BadRequestException) {
            UnitResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            UnitResponse.unauthorized(uae.message)
        } catch (nfe: NotFoundException) {
            UnitResponse.notFound(nfe.message)
        }
    }
    fun getAllLanguages(native: String, depth: DepthRequest): LanguageResponse{
        return try {
            validateDepthOrThrowException(depth.depth)
            checkNativeExistsOrThrowException(native)
            val languages = dataDao.getLanguages(native, depth.depth)
           LanguageResponse.success(languages.map { Language.create(it)})
        } catch (bre: BadRequestException) {
            LanguageResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            LanguageResponse.unauthorized(uae.message)
        }
    }

    fun addLanguage(user: User, language: LanguageRequest): IntIdResponse {
        return try {
            val nativeId = language.nativeId
            val languageId  = language.languageId
            val languageName = language.languageName
            val flag = language.flag
//            validateLanguageRequestOrThrowException(nativeId, languageId, languageName, flag)
//            validateAdmin(user)
            val responseId = dataDao.addLanguage(nativeId, languageId, languageName, flag)
            IntIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
           IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }
    fun updateLanguage(user: User, id: Int, language: LanguageRequest): IntIdResponse {
        return try {
            val nativeId = language.nativeId
            val languageId  = language.languageId
            val languageName = language.languageName
            val flag = language.flag
            validateLanguageRequestOrThrowException(nativeId, languageId, languageName, flag)
            validateAdmin(user)
            val responseId = dataDao.updateLanguage(id, nativeId, languageId, languageName, flag)
            IntIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }
    fun deleteLanguage(user: User, languageId: Int): IntIdResponse {
        return try {
            checkDataExistsOrThrowException(languageId, EntityLanguage)
            validateAdmin(user)

            if (dataDao.deleteLanguage(languageId)) {
                IntIdResponse.success(languageId)
            } else {
                IntIdResponse.failed("Error Occurred")
            }
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            IntIdResponse.notFound(nfe.message)
        }
    }

    fun addUnit(user: User, unit: UnitRequest): IntIdResponse {
        return try {
            val title = unit.title
            val order  = unit.order
            val language = unit.languageId
            validateUnitRequestOrThrowException(title, order, language)
            validateAdmin(user)
            val responseId = dataDao.addUnit(title, order, language)
            IntIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }
    fun updateUnit(user: User, unitId: Int, unit: UnitRequest): IntIdResponse {
        return try {
            val title = unit.title
            val order  = unit.order
            val language = unit.languageId
            validateUnitRequestOrThrowException(title, order, language)
            validateAdmin(user)
            val responseId = dataDao.updateUnit(unitId, title, order, language)
            IntIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }
    fun deleteUnit(user: User, unitId: Int): IntIdResponse {
        return try {
            checkDataExistsOrThrowException(unitId, EntityUnit)
            validateAdmin(user)
            if (dataDao.deleteUnit(unitId)) {
                IntIdResponse.success(unitId)
            } else {
                IntIdResponse.failed("Error Occurred")
            }
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            IntIdResponse.notFound(nfe.message)
        }
    }

//    fun addSection(section: SectionRequest): IntIdResponse {
//
//    }
//    fun updateSection(section: SectionRequest): IntIdResponse {
//
//    }
//    fun deleteSection(sectionId: Int): IntIdResponse {
//
//    }
//
//    fun addWord(word: WordRequest): IntIdResponse {
//
//    }
//    fun updateWord(word: WordRequest): IntIdResponse {
//
//    }
//    fun deleteWord(wordId: Int): IntIdResponse {
//
//    }
//
//    fun addSentence(sentence: SentenceRequest): IntIdResponse {
//
//    }
//    fun updateSentence(sentence: SentenceRequest): IntIdResponse {
//
//    }
//    fun deleteSentence(sentenceId: Int): IntIdResponse {
//
//    }

    private fun validateDepthOrThrowException(depth: Int) {
        val message = when {
            depth < 0 -> "Depth does not exist"
            else -> return
        }
        throw BadRequestException(message)
    }

    private fun validateLanguageRequestOrThrowException(nativeId: String, languageId: String, languageName: String, flag: String){
        val message = when {
            nativeId.length != 2 -> "Native language id must be 2 characters"
            languageId.length != 2 -> "Language id must be 2 characters"
            flag.length != 2 -> "Flag id must be 2 characters"
            languageName.length > 30 -> "Language name is too long"
            !languageName.isNumeric() -> "Language name cannot contain numbers"
            !dataDao.flagExists(flag) -> "Flag does not exist"
            else -> return
        }
        throw BadRequestException(message)
    }
    private fun validateUnitRequestOrThrowException(title: String, order: Int, languageId: Int){
        val message = when {
            !title.isNumeric()-> "'title' cannot contain numbers"
            order < 0 -> "'order' cannot be negative"
            !dataDao.exists(languageId, EntityLanguage) -> "Language does not exist"
            else -> return
        }
        throw BadRequestException(message)
    }
    private fun validateAdmin(user: User){
        val message = when {
            !user.isAdmin -> "You must be an admin to add languages"
            else -> return
        }
        throw UnauthorizedActivityException(message)
    }
    private fun checkDataExistsOrThrowException(dataId: Int, type: Any) {
        if (!dataDao.exists(dataId, type)) {
            throw NotFoundException("Data not exist with ID '$dataId'")
        }
    }
    private fun checkNativeExistsOrThrowException(native: String) {
        val message = when {
            native.length != 2 -> "Native language id must be 2 characters"
            !dataDao.nativeExists(native) -> "Native id does not exist"
            else -> return
        }
        throw BadRequestException(message)
    }


}