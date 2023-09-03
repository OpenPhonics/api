/*
 * Copyright 2020 Shreyas Patil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.openphonics.data.model.user

import com.openphonics.data.entity.user.EntityUser

data class User(
    val id: String,
    val name: String,
    val classCode: String,
    val native: String,
    val currentLanguage: String?,
    val isAdmin: Boolean
) {
    companion object {
        fun fromEntity(entity: EntityUser) = User(
            entity.id.value.toString(),
            entity.name,
            entity.classCode.value,
            entity.native,
            entity.currentLanguage?.value.toString(),
            entity.isAdmin
        )

    }
}