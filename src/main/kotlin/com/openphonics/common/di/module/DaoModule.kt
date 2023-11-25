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

package com.openphonics.common.di.module

import com.openphonics.course.CourseDAO
import com.openphonics.course.CourseDAOImpl
import com.openphonics.courseword.CourseWordDAO
import com.openphonics.courseword.CourseWordDAOImpl
import com.openphonics.language.LanguageDAO
import com.openphonics.language.LanguageDAOImpl
import com.openphonics.word.WordDAO
import com.openphonics.word.WordDAOImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DAOModule {
    @Singleton
    @Binds
    fun languageDAO(dao: LanguageDAOImpl): LanguageDAO
    @Singleton
    @Binds
    fun wordDao(dao: WordDAOImpl): WordDAO
    @Singleton
    @Binds
    fun courseDao(dao: CourseDAOImpl): CourseDAO
    @Singleton
    @Binds
    fun courseWordDao(dao: CourseWordDAOImpl): CourseWordDAO
}