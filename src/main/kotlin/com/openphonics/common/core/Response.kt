package com.openphonics.common.core

import com.openphonics.language.LanguageBase
import kotlinx.serialization.Serializable
interface Response {
    val status: State
    val message: String
}

@Serializable
data class FailureResponse(override val status: State, override val message: String) : Response
enum class State {
    SUCCESS, NOT_FOUND, FAILED, UNAUTHORIZED
}
@Serializable
data class DataResponse<T>(
    override val status: State,
    override val message: String,
    val data: List<T> = emptyList()
) : Response {
    companion object {
        fun <T> unauthorized(message: String) = DataResponse<T>(
            State.UNAUTHORIZED,
            message
        )
        fun <T> success(data: List<T>) = DataResponse(
            State.SUCCESS,
            "Task successful",
            data
        )
        fun <T> success(data: T) = DataResponse(
            State.SUCCESS,
            "Task successful",
            listOf(data)
        )

        fun <T> failed(message: String) = DataResponse<T>(
            State.FAILED,
            message
        )

        fun <T> notFound(message: String) = DataResponse<T>(
            State.NOT_FOUND,
            message
        )
    }
}

@Serializable
data class IdResponse(
    override val status: State,
    override val message: String,
    val id: Int? = null
) : Response {
    companion object {
        fun unauthorized(message: String) = IdResponse(
            State.UNAUTHORIZED,
            message
        )

        fun failed(message: String) = IdResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = IdResponse(
            State.NOT_FOUND,
            message
        )

        fun success(id: Int) = IdResponse(
            State.SUCCESS,
            "Task successful",
            id
        )
    }
}