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

import dagger.Lazy
import dagger.Subcomponent
import com.openphonics.application.di.module.DaoModule
import com.openphonics.data.flag.FlagDao
import com.openphonics.data.language.LanguageDao
import com.openphonics.data.word.WordDao
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = [DaoModule::class])
interface DaoComponent {
    fun flagDao(): Lazy<FlagDao>
    fun languageDao(): Lazy<LanguageDao>
    fun wordDao(): Lazy<WordDao>
}