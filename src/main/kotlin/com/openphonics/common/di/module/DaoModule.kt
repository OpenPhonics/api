package com.openphonics.common.di.module

import com.openphonics.auth.UserDao
import com.openphonics.auth.UserDaoImpl
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

    @Singleton
    @Binds
    fun userDao(dao: UserDaoImpl): UserDao
}