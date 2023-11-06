package com.openphonics.application.response

import com.openphonics.data.word.Word
import kotlinx.serialization.Serializable

@Serializable
data class WordResponse(
    override val status: State,
    override val message: String,
    val word: List<Word> = emptyList()
)  : Response {
    companion object {
        fun unauthorized(message: String) = WordResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(word: List<Word>) = WordResponse(
            State.SUCCESS,
            "Task successful",
            word
        )
        fun success(word: Word) = WordResponse(
            State.SUCCESS,
            "Task successful",
            listOf(word)
        )

        fun failed(message: String) = WordResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = WordResponse(
            State.NOT_FOUND,
            message
        )
    }
}