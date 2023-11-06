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

package com.openphonics.application.di.module

import com.openphonics.data.flag.FlagDao
import com.openphonics.data.flag.FlagDaoImpl
import com.openphonics.data.language.LanguageDao
import com.openphonics.data.language.LanguageDaoImpl
import com.openphonics.data.word.WordDao
import com.openphonics.data.word.WordDaoImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DaoModule {
    @Singleton
    @Binds
    fun flagDao(dao: FlagDaoImpl): FlagDao
    @Singleton
    @Binds
    fun languageDao(dao: LanguageDaoImpl): LanguageDao
    @Singleton
    @Binds
    fun wordDao(dao: WordDaoImpl): WordDao
}