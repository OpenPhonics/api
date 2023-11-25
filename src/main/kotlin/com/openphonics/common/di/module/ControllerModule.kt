package com.openphonics.common.di.module

import com.openphonics.course.CourseController
import com.openphonics.course.CourseControllerImpl
import com.openphonics.courseword.CourseWordController
import com.openphonics.courseword.CourseWordControllerImpl
import com.openphonics.language.LanguageController
import com.openphonics.language.LanguageControllerImpl
import com.openphonics.word.WordController
import com.openphonics.word.WordControllerImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
interface ControllerModule {
    @Singleton
    @Binds
    fun wordController(controller: WordControllerImpl): WordController
    @Singleton
    @Binds
    fun languageController(controller: LanguageControllerImpl): LanguageController

    @Singleton
    @Binds
    fun courseController(controller: CourseControllerImpl): CourseController

    @Singleton
    @Binds
    fun courseWordController(controller: CourseWordControllerImpl): CourseWordController

}