package com.openphonics.controller

import com.openphonics.exception.BadRequestException
import com.openphonics.exception.UnauthorizedActivityException
import com.openphonics.exception.NotFoundException
import com.openphonics.model.response.LanguageProgress
import com.openphonics.model.response.LanguageProgressResponse
import com.openphonics.data.dao.DataDaoImpl
import com.openphonics.data.dao.ProgressDaoImpl
import com.openphonics.data.entity.data.EntityLanguage
import com.openphonics.data.entity.progress.EntityLanguageProgress
import com.openphonics.data.entity.progress.EntitySectionProgress
import com.openphonics.data.entity.references.EntitySectionProgressLearnedWordCrossRef
import com.openphonics.data.model.user.User
import com.openphonics.model.request.*
import com.openphonics.model.response.StrIdResponse
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressController @Inject constructor(
    private val progressDao: ProgressDaoImpl,
    private val dataDao: DataDaoImpl
) {

    fun getLanguageProgress(user: User, languageProgressId: String, depth: DepthRequest): LanguageProgressResponse {
        return try {
            validateDepthOrThrowException(depth.depth)
            checkProgressExistsOrThrowException(languageProgressId, EntityLanguageProgress)
            checkOwnerOrThrowException(user.id, languageProgressId, EntityLanguageProgress)
            val languageProgress = progressDao.getLanguageProgress(languageProgressId, depth.depth)!!
            LanguageProgressResponse.success(LanguageProgress.create(languageProgress))
        } catch (bre: BadRequestException) {
            LanguageProgressResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            LanguageProgressResponse.unauthorized(uae.message)
        } catch (nfe: NotFoundException) {
            LanguageProgressResponse.notFound(nfe.message)
        }
    }
    fun getAllProgressByUser(user: User, depth: DepthRequest): LanguageProgressResponse {
        return try {
            validateDepthOrThrowException(depth.depth)
            val languagesProgress = progressDao.getLanguageProgressByUser(user.id, depth.depth)
            LanguageProgressResponse.success(languagesProgress.map {LanguageProgress.create (it)})
        } catch (bre: BadRequestException) {
            LanguageProgressResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            LanguageProgressResponse.unauthorized(uae.message)
        } catch (nfe: NotFoundException) {
            LanguageProgressResponse.notFound(nfe.message)
        }
    }
    fun addLanguageProgress(user: User, language: LanguageProgressRequest): StrIdResponse {
        return try {
            checkDataExistsOrThrowException(language.languageId, EntityLanguage)
            if (progressDao.languageProgressExists(language.languageId, user.id)){
                throw BadRequestException("User already signed up for this language")
            }
            val languageProgressId = progressDao.addLanguageProgress(user.id, language.languageId)
            StrIdResponse.success(languageProgressId)
        } catch (bre: BadRequestException) {
            StrIdResponse.failed(bre.message)
        }
    }
    fun updateLanguageProgress(user: User, languageProgressId: String, languageProgress: XPRequest): StrIdResponse {
        return try {
            validateLanguageProgressRequestOrThrowException(languageProgress.xp)
            checkProgressExistsOrThrowException(languageProgressId, EntityLanguageProgress)
            checkOwnerOrThrowException(user.id, languageProgressId, EntityLanguageProgress)
            val id = progressDao.updateLanguageProgress(languageProgressId, languageProgress.xp)
            StrIdResponse.success(id)
        } catch (uae: UnauthorizedActivityException) {
            StrIdResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            StrIdResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            StrIdResponse.notFound(nfe.message)
        }
    }
    fun updateStreak(user: User, languageProgressId: String, streak: StreakRequest): StrIdResponse {
        return try {

            checkProgressExistsOrThrowException(languageProgressId, EntityLanguageProgress)
            val updated = progressDao.getLanguageProgress(languageProgressId, 0)!!.updated
            val updatedTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(updated), ZoneId.systemDefault())
            val nowTime =  LocalDateTime.now().toLocalDate()
            if (nowTime.dayOfYear == updatedTime.dayOfYear && nowTime.year == updatedTime.year){
                throw BadRequestException("You cannot update the streak")
            }
            checkOwnerOrThrowException(user.id, languageProgressId, EntityLanguageProgress)
            val id = progressDao.updateStreak(languageProgressId, streak.continueStreak)
            StrIdResponse.success(id)
        } catch (uae: UnauthorizedActivityException) {
            StrIdResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            StrIdResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            StrIdResponse.notFound(nfe.message)
        }
    }
    fun deleteLanguageProgress(user: User, languageProgressId: String): StrIdResponse {
        return try {
            checkProgressExistsOrThrowException(languageProgressId, EntityLanguageProgress)
            checkOwnerOrThrowException(user.id, languageProgressId, EntityLanguageProgress)
            if (progressDao.deleteLanguageProgress(languageProgressId)) {
                StrIdResponse.success(languageProgressId)
            } else {
                StrIdResponse.failed("Error Occurred")
            }
        } catch (uae: UnauthorizedActivityException) {
            StrIdResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            StrIdResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            StrIdResponse.notFound(nfe.message)
        }
    }
    fun updateSectionProgress(user: User, sectionProgressId: String, section: SectionProgressRequest): StrIdResponse {
        return try {
            val currentLesson = section.currentLesson
            val isLegendary = section.isLegendary
            val learnedWords = section.learnedWords
            validateSectionProgressRequestOrThrowException(currentLesson, isLegendary, learnedWords)
            checkProgressExistsOrThrowException(sectionProgressId, EntitySectionProgress)
            checkOwnerOrThrowException(user.id, sectionProgressId, EntitySectionProgress)
            val id = progressDao.updateSectionProgress(sectionProgressId, currentLesson, isLegendary, learnedWords)
            StrIdResponse.success(id)
        } catch (uae: UnauthorizedActivityException) {
            StrIdResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            StrIdResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            StrIdResponse.notFound(nfe.message)
        }
    }

    private fun validateDepthOrThrowException(depth: Int) {
        val message = when {
            depth < 0 -> "Depth does not exist"
            else -> return
        }
        throw BadRequestException(message)
    }
    private fun checkProgressExistsOrThrowException(progressId: String, type: Any) {
        if (!progressDao.exists(progressId, type)) {
            throw NotFoundException("Progress not exist with ID '$progressId'")
        }
    }
    private fun checkDataExistsOrThrowException(dataId: Int, type: Any) {
        if (!dataDao.exists(dataId, type)) {
            throw BadRequestException("Data not exist with ID '$dataId'")
        }
    }
    private fun checkOwnerOrThrowException(userId: String, progressId: String, type: Any) {
        if (!progressDao.isOwnedByUser(progressId, userId, type)) {
            throw UnauthorizedActivityException("Access denied")
        }
    }
    private fun validateLanguageProgressRequestOrThrowException(xp: Int) {
        val message = when {
            xp <= 0 -> "XP should not decrease"
            else -> return
        }
        throw BadRequestException(message)
    }
    private fun validateSectionProgressRequestOrThrowException(currentLesson: Int, isLegendary: Boolean, learnedWords: List<Int>) {
        val message = when {
            currentLesson < 1 -> "Invalid lesson number"
            else -> return
        }
        learnedWords.forEach {
            if (!dataDao.exists(it, EntitySectionProgressLearnedWordCrossRef)) {
                throw BadRequestException("Word does not exist with ID '$it'")
            }
        }
        throw BadRequestException(message)
    }
}