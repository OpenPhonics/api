package com.openphonics.data.model.user

import com.openphonics.data.entity.user.EntityClassroom



data class Classroom(
    val className: String,
    val students: List<Student>,
    val id: String
) {
    companion object {
        fun fromEntity(entity: EntityClassroom) = Classroom(
            entity.className,
            entity.students.map {
                Student.fromEntity(it)
            },
            entity.id.value
        )
    }
}