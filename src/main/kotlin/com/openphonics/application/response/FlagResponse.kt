package com.openphonics.application.response

import com.openphonics.data.flag.Flag
import kotlinx.serialization.Serializable

@Serializable
data class FlagResponse(
    override val status: State,
    override val message: String,
    val flag: List<Flag> = emptyList()
)  : Response {
    companion object {
        fun unauthorized(message: String) = FlagResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(flag: List<Flag>) = FlagResponse(
            State.SUCCESS,
            "Task successful",
            flag
        )
        fun success(flag: Flag) = FlagResponse(
            State.SUCCESS,
            "Task successful",
            listOf(flag)
        )

        fun failed(message: String) = FlagResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = FlagResponse(
            State.NOT_FOUND,
            message
        )
    }
}