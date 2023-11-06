package com.openphonics.application.response

import kotlinx.serialization.Serializable


@Serializable
data class IntResponse(
    override val status: State,
    override val message: String,
    val id: Int? = null
) : Response {
    companion object {
        fun unauthorized(message: String) = IntResponse(
            State.UNAUTHORIZED,
            message
        )

        fun failed(message: String) = IntResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = IntResponse(
            State.NOT_FOUND,
            message
        )

        fun success(id: Int) = IntResponse(
            State.SUCCESS,
            "Task successful",
            id
        )
    }
}