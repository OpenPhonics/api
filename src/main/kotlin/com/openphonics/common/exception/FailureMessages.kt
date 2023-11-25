package com.openphonics.common.exception

object FailureMessages {
    const val MESSAGE_MISSING_UPDATE_FLAG_DETAILS = "Required 'flag' missing."
    const val MESSAGE_MISSING_FLAG_DETAILS = "Required 'flag' or 'id' missing."

    const val MESSAGE_MISSING_LANGUAGE_DETAILS = "Required 'native id' or 'language id' or 'language name' or 'flag' missing."
    const val MESSAGE_MISSING_WORD_DETAILS = "Required 'phonic' or 'sound' or 'translated sound' or 'translated word' or 'word' or 'language' missing."
    const val MESSAGE_MISSING_UPDATE_WORD_DETAILS = "Required 'phonic' or 'sound' or 'translated sound' or 'translated word' or 'word' missing."
    const val MESSAGE_FAILED = "Something went wrong!"
}