package com.openphonics.common.core

import com.openphonics.common.exception.BadRequestException
import com.openphonics.common.exception.NotFoundException
import org.jetbrains.exposed.dao.IntEntity

typealias CRUDController<Template, Create, Update, Base> = CRUD<Template, Create, Update, DataResponse<Base>, DataResponse<Int>, DataResponse<Int>>

abstract class Controller<Template, Create : Template, Update : Template, Base : Template, Entity : IntEntity>(
    protected open val dao: DAO<Template, Create, Update, Base, Entity>
) : CRUDController<Template, Create, Update, Base> {
    protected fun existsOrThrowException(id: Int): Base {
        return dao.get(id) ?: throw NotFoundException("id $id does not exist")
    }
    protected abstract fun validateOrThrowException(id: Int, request: Update)
    protected abstract fun validateOrThrowException(request: Create)
    override fun get(id: Int): DataResponse<Base> {
        return try {
            existsOrThrowException(id)
            val data = dao.get(id)!!
            DataResponse.success(data)
        } catch (nfe: NotFoundException) {
            DataResponse.notFound(nfe.message)
        }
    }

    override fun create(data: Create): DataResponse<Int> {
        return try {
            validateOrThrowException(data)
            val responseId = dao.create(data)
            DataResponse.success(responseId)
        } catch (bre: BadRequestException) {
            DataResponse.failed(bre.message)
        }
    }

    override fun update(id: Int, data: Update): DataResponse<Int> {
        return try {
            validateOrThrowException(id, data)
            val responseId = dao.update(id, data)
            DataResponse.success(responseId)
        } catch (bre: BadRequestException) {
            DataResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            DataResponse.notFound(nfe.message)
        }
    }

    override fun delete(id: Int): DataResponse<Int> {
        return try {
            if (dao.delete(id)) {
                DataResponse.success(id)
            } else {
                DataResponse.failed("Error Occurred")
            }
        } catch (bre: BadRequestException) {
            DataResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            DataResponse.notFound(nfe.message)
        }
    }
}