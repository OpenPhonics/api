package com.openphonics.data.model.user

import com.openphonics.data.entity.user.EntityUser
import com.openphonics.data.model.progress.LanguageProgress

data class Student(
    val name: String,
    val classCode: String,
    val native: String,
    val progress: List<LanguageProgress>,
){
    companion object {
        fun fromEntity(entity: EntityUser) = Student(
            entity.name,
            entity.classCode.value,
            entity.native,
            entity.progress.map {
                LanguageProgress.fromEntity(it, depth = LanguageProgress.LANGUAGE)
            }
        )
    }
}