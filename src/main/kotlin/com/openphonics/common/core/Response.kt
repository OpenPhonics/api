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
    val data: T? = null
) : Response {
    companion object {
        fun <T> unauthorized(message: String) = DataResponse<T>(
            State.UNAUTHORIZED,
            message
        )
        fun <T> success(data: T) = DataResponse(
            State.SUCCESS,
            "Task successful",
            data
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