package com.openphonics.application.response

import com.openphonics.data.language.Language
import kotlinx.serialization.Serializable

@Serializable
data class LanguageResponse(
    override val status: State,
    override val message: String,
    val language: List<Language> = emptyList()
)  : Response {
    companion object {
        fun unauthorized(message: String) = LanguageResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(language: List<Language>) = LanguageResponse(
            State.SUCCESS,
            "Task successful",
            language
        )
        fun success(language: Language) = LanguageResponse(
            State.SUCCESS,
            "Task successful",
            listOf(language)
        )

        fun failed(message: String) = LanguageResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = LanguageResponse(
            State.NOT_FOUND,
            message
        )
    }
}