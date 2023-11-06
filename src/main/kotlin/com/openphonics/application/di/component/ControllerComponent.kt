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

package com.openphonics.application.di.component

import com.openphonics.application.controller.FlagController
import com.openphonics.application.controller.LanguageController
import com.openphonics.application.controller.WordController
import dagger.Lazy
import dagger.Subcomponent
import com.openphonics.application.di.module.ConfigModule
import com.openphonics.application.di.module.DaoModule
import io.ktor.util.KtorExperimentalAPI
import javax.inject.Singleton

@KtorExperimentalAPI
@Singleton
@Subcomponent(modules = [ConfigModule::class, DaoModule::class])
interface ControllerComponent {
    fun flagController(): Lazy<FlagController>
    fun languageController(): Lazy<LanguageController>
    fun wordController(): Lazy<WordController>
}