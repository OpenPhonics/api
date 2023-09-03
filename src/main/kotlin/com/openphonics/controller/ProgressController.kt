package com.openphonics.controller

import com.openphonics.exception.BadRequestException
import com.openphonics.exception.UnauthorizedActivityException
import com.openphonics.exception.NotFoundException
import com.openphonics.model.request.DepthRequest
import com.openphonics.model.response.LanguageProgress
import com.openphonics.model.response.LanguageProgressResponse
import com.openphonics.data.dao.DataDaoImpl
import com.openphonics.data.dao.ProgressDaoImpl
import com.openphonics.data.entity.progress.EntityLanguageProgress
import com.openphonics.data.entity.references.EntitySectionProgressLearnedWordCrossRef
import com.openphonics.data.model.user.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressController @Inject constructor(
    private val progressDao: ProgressDaoImpl,
    private val dataDao: DataDaoImpl
) {

    fun getLanguageProgress(
        user: User,
        languageProgressId: String,
        depth: DepthRequest
    ): LanguageProgressResponse {
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
    fun getAllProgressByUser(
        user: User,
        depth: DepthRequest
    ): LanguageProgressResponse {
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

//
//    fun addLanguageProgress(user: User, language: CreateLanguageProgressRequest): DataResponse<String> {
//        return try {
//            checkDataExistsOrThrowException(language.id, EntityLanguage)
//            val languageProgressId = progressDao.addLanguageProgress(user.id, language.id)
//            DataResponse.success(languageProgressId)
//        } catch (bre: BadRequestException) {
//            DataResponse.failed(bre.message)
//        }
//    }
//
//
//    fun getLanguageProgressByUser(user: User): DataResponse<List<SerializableLanguageProgress>> {
//        return try {
//            val languagesProgress = progressDao.getLanguageProgressByUser(user.id)
//
//            DataResponse.success(languagesProgress.map {
//                SerializableLanguageProgress.create(it)
//            })
//        } catch (uae: UnauthorizedActivityException) {
//            DataResponse.unauthorized(uae.message)
//        }
//    }
//
//    fun updateLanguageProgress(
//        user: User,
//        languageProgressId: String,
//        languageProgress: LanguageProgressRequest
//    ): DataResponse<String> {
//        return try {
//            validateLanguageProgressRequestOrThrowException(languageProgress.xp)
//            checkProgressExistsOrThrowException(languageProgressId, EntityLanguageProgress)
//            checkOwnerOrThrowException(user.id, languageProgressId, EntityLanguageProgress)
//            val id = progressDao.updateLanguageProgress(languageProgressId, languageProgress.xp)
//            DataResponse.success(id)
//        } catch (uae: UnauthorizedActivityException) {
//            DataResponse.unauthorized(uae.message)
//        } catch (bre: BadRequestException) {
//            DataResponse.failed(bre.message)
//        } catch (nfe: NotFoundException) {
//            DataResponse.notFound(nfe.message)
//        }
//    }
//
//    fun deleteLanguageProgress(user: User, languageProgressId: String): DataResponse<String> {
//        return try {
//            checkProgressExistsOrThrowException(languageProgressId, EntityLanguageProgress)
//            checkOwnerOrThrowException(user.id, languageProgressId, EntityLanguageProgress)
//
//            if (progressDao.deleteLanguageProgress(languageProgressId)) {
//                DataResponse.success(languageProgressId)
//            } else {
//                DataResponse.failed("Error Occurred")
//            }
//        } catch (uae: UnauthorizedActivityException) {
//            DataResponse.unauthorized(uae.message)
//        } catch (bre: BadRequestException) {
//            DataResponse.failed(bre.message)
//        } catch (nfe: NotFoundException) {
//            DataResponse.notFound(nfe.message)
//        }
//    }
//
//    fun updateStreak(
//        user: User,
//        languageProgressId: String,
//        streakRequest: StreakRequest
//    ): DataResponse<String> {
//        return try {
//            checkProgressExistsOrThrowException(languageProgressId, EntityLanguageProgress)
//            checkOwnerOrThrowException(user.id, languageProgressId, EntityLanguageProgress)
//            val id = progressDao.updateStreak(languageProgressId, streakRequest.streakContinued)
//            DataResponse.success(id)
//        } catch (uae: UnauthorizedActivityException) {
//            DataResponse.unauthorized(uae.message)
//        } catch (bre: BadRequestException) {
//            DataResponse.failed(bre.message)
//        } catch (nfe: NotFoundException) {
//            DataResponse.notFound(nfe.message)
//        }
//    }
//
//    fun updateUnitProgress(user: User, unitProgressId: String): DataResponse<String> {
//        TODO("Not yet implemented")
//    }
//
//    fun updateSectionProgress(
//        user: User,
//        sectionProgressId: String,
//        sectionProgress: SectionProgressRequest
//    ): DataResponse<String> {
//        return try {
//            validateSectionProgressRequestOrThrowException(
//                sectionProgress.currentLesson,
//                sectionProgress.isLegendary,
//                sectionProgress.learnedWords
//            )
//            checkProgressExistsOrThrowException(sectionProgressId, EntitySectionProgress)
//            checkOwnerOrThrowException(user.id, sectionProgressId, EntitySectionProgress)
//            val id = progressDao.updateSectionProgress(
//                sectionProgressId,
//                sectionProgress.currentLesson,
//                sectionProgress.isLegendary,
//                sectionProgress.learnedWords
//            )
//            DataResponse.success(id)
//        } catch (uae: UnauthorizedActivityException) {
//            DataResponse.unauthorized(uae.message)
//        } catch (bre: BadRequestException) {
//            DataResponse.failed(bre.message)
//        } catch (nfe: NotFoundException) {
//            DataResponse.notFound(nfe.message)
//        }
//    }

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

    private fun validateSectionProgressRequestOrThrowException(
        currentLesson: Int,
        isLegendary: Boolean,
        learnedWords: List<Int>
    ) {
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