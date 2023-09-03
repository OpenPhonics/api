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

package com.openphonics.data.entity.user

import com.openphonics.data.database.table.progress.LanguagesProgress
import com.openphonics.data.database.table.user.Users
import com.openphonics.data.entity.progress.EntityLanguageProgress
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class EntityUser(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EntityUser>(Users)

    var name by Users.name
    var classCode by Users.classCode
    var native by Users.native
    var currentLanguage by Users.currentLanguage
    var isAdmin by Users.isAdmin

    val progress by lazy {
        transaction {
            EntityLanguageProgress.find {
                LanguagesProgress.user eq id
            }
        }
    }
}