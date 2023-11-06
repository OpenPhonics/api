package com.openphonics.application.response

import kotlinx.serialization.Serializable


@Serializable
data class IntIdResponse(
    override val status: State,
    override val message: String,
    val id: Int? = null
) : Response {
    companion object {
        fun unauthorized(message: String) = IntIdResponse(
            State.UNAUTHORIZED,
            message
        )

        fun failed(message: String) = IntIdResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = IntIdResponse(
            State.NOT_FOUND,
            message
        )

        fun success(id: Int) = IntIdResponse(
            State.SUCCESS,
            "Task successful",
            id
        )
    }
}