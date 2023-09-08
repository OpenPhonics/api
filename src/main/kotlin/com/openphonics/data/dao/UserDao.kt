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

package com.openphonics.data.dao

import com.openphonics.data.database.table.progress.LanguagesProgress
import com.openphonics.data.database.table.user.Classrooms
import com.openphonics.data.database.table.user.Users
import com.openphonics.data.entity.data.EntityLanguage
import com.openphonics.data.entity.progress.EntityLanguageProgress
import com.openphonics.data.entity.user.EntityClassroom
import com.openphonics.data.entity.user.EntityUser
import com.openphonics.data.model.user.User
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.swing.text.html.parser.Entity

interface UserDao {

    fun addClass(classCode: String, className: String): String
    fun addAdmin(name: String, classCode: String, native: String): User
    fun addUser(name: String, classCode: String, native: String, language: Int): User
    fun findByUUID(uuid: UUID): User?
    fun findByNameAndClassCode(name: String, classCode: String): User?
    fun exists(uuid: UUID): Boolean
    fun isNameAvailable(name: String, classCode: String): Boolean

    fun doesClassCodeExists(classCode: String): Boolean

    fun deleteByID(id: String): Boolean
}


@Singleton
class UserDaoImpl @Inject constructor() : UserDao {
    override fun addClass(classCode: String, className: String): String = transaction{
       EntityClassroom.new (classCode) {
           this.className = className
       }.id.value
    }

    override fun addAdmin(name: String, classCode: String, native: String): User= transaction {
        EntityUser.new {
            this.name = name
            this.classCode = EntityID(classCode, Classrooms)
            this.native = native
            this.isAdmin = true
            this.currentLanguage = null
        }
    }.let { User.fromEntity(it)}
    override fun addUser(name: String, classCode: String, native: String, language: Int): User = transaction {

        val user = EntityUser.new {
            this.name = name
            this.classCode = EntityID(classCode, Classrooms)
            this.native = native
            this.isAdmin = false
            this.currentLanguage = null
        }
        val lang = EntityLanguageProgress.new {
            this.user = EntityUser[user.id.value]
            this.language = EntityLanguage[language]
        }.id.value

        EntityLanguageProgress.find {
            (LanguagesProgress.id eq lang)
        }.firstOrNull()?.let {
            user.apply {
                this.currentLanguage = it.id
            }
        }
        return@transaction user
    }.let { User.fromEntity(it)}



    override fun findByUUID(uuid: UUID): User? = transaction {
        EntityUser.findById(uuid)
    }?.let { User.fromEntity(it) }

    override fun findByNameAndClassCode(name: String, classCode: String): User? = transaction {
        EntityUser.find {
            (Users.name eq name) and (Users.classCode eq classCode)
        }.firstOrNull()
    }?.let { User.fromEntity(it) }

    override fun isNameAvailable(name: String, classCode: String): Boolean {
        return transaction {
            EntityUser.find { (Users.name eq name) and (Users.classCode eq classCode) }.firstOrNull()
        } == null
    }

    override fun doesClassCodeExists(classCode: String): Boolean = transaction {
            EntityClassroom.findById(classCode) != null
        }

    override fun deleteByID(id: String): Boolean = transaction {
        EntityUser.findById(UUID.fromString(id))?.let{
            it.run {
                delete()
                return@transaction true
            }
        }
        return@transaction false
    }

    override fun exists(uuid: UUID): Boolean = findByUUID(uuid) != null

//    override fun getClassroomByClassCode(classCode: String): ClassroomData? = transaction {
//        EntityClassroom.find {
//            (Classrooms.classCode eq classCode)
//        }.firstOrNull()
//    }?.let {
//        ClassroomData.fromEntity(it, EntityUser.find {
//            Users.classCode eq classCode
//        }.map { user ->
//            StudentData(user.name,
//                EntityLanguageProgress
//                    .find { LanguagesProgress.user eq user.id }
//                    .map {
//                        LanguageProgress.fromEntity(it)
//                    }
//            )
//        })
//    }

}