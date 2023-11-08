package com.openphonics.application.controller

import com.openphonics.application.exception.BadRequestException
import com.openphonics.application.exception.NotFoundException
import com.openphonics.application.request.FlagRequest
import com.openphonics.application.request.UpdateFlagRequest
import com.openphonics.application.response.FlagResponse
import com.openphonics.application.response.IntResponse
import com.openphonics.application.response.StrResponse
import com.openphonics.data.flag.FlagDao
import com.openphonics.utils.OpenPhonicsRequestValidator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlagController @Inject constructor(
    private val flagDao: FlagDao,
    private val validator: OpenPhonicsRequestValidator
){
    fun all(): FlagResponse {
        val flags = flagDao.all()
        return FlagResponse.success(flags)
    }
    fun get(id: String): FlagResponse {
        return try {
            validator.flagExistsOrThrowException(id)
            val flag = flagDao.get(id)!!
            FlagResponse.success(flag)
        } catch (nfe: NotFoundException) {
            FlagResponse.notFound(nfe.message)
        }
    }
    fun create(request: FlagRequest): StrResponse {
        with(request) {
            return try {
                validator.validateOrThrowException(request)
                val responseId = flagDao.create(id, flag)
                StrResponse.success(responseId)
            } catch (bre: BadRequestException) {
                StrResponse.failed(bre.message)
            }
        }
    }
    fun update(id: String, request: UpdateFlagRequest): StrResponse {
        with(request) {
            return try {
                validator.validateOrThrowException(id, request)
                val responseId = flagDao.update(id, flag)
                StrResponse.success(responseId)
            } catch (bre: BadRequestException) {
                StrResponse.failed(bre.message)
            } catch (nfe: NotFoundException) {
                StrResponse.notFound(nfe.message)
            }
        }
    }
    fun delete(id: String): StrResponse {
        return try {
            if (flagDao.delete(id)) {
                StrResponse.success(id)
            } else {
                StrResponse.failed("Error Occurred")
            }
        } catch (nfe: NotFoundException) {
            StrResponse.notFound(nfe.message)
        } catch (bre: BadRequestException) {
            StrResponse.notFound(bre.message)
        }
    }
}