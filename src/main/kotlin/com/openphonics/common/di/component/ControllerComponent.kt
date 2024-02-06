/*
 * Copyright 2021 Shreyas Patil
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

package com.openphonics.common.di.component

import com.openphonics.auth.AuthController
import com.openphonics.auth.JWTController
import com.openphonics.common.di.module.ConfigModule
import com.openphonics.common.di.module.ControllerModule
import com.openphonics.common.di.module.DAOModule
import com.openphonics.common.di.module.EncryptorModule
import com.openphonics.course.CourseController
import com.openphonics.courseword.CourseWordController
import dagger.Lazy
import dagger.Subcomponent
import com.openphonics.language.LanguageController
import com.openphonics.language.LanguageDAO
import com.openphonics.word.WordController
import io.ktor.util.KtorExperimentalAPI
import javax.inject.Singleton

@KtorExperimentalAPI
@Singleton
@Subcomponent(modules = [ControllerModule::class, DAOModule::class, EncryptorModule::class, ConfigModule::class])
interface ControllerComponent {
    fun languageController(): Lazy<LanguageController>
    fun wordController(): Lazy<WordController>
    fun courseController(): Lazy<CourseController>
    fun authController(): Lazy<AuthController>
    fun courseWordController(): Lazy<CourseWordController>
    fun jwtController(): Lazy<JWTController>

}