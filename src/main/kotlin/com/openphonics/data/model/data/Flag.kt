package com.openphonics.data.model.data

import com.openphonics.data.entity.data.EntityFlag

data class Flag(
    val flag: String,
    val id: String
){
    companion object {
        fun fromEntity(entity: EntityFlag): Flag = Flag(
            entity.flag,
            entity.id.value
        )
    }
}