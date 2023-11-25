package com.openphonics.course

interface CourseOperations<MultiBase> {
    fun all(languageCode: String): MultiBase
}
