package com.openphonics.application.di.module

import com.openphonics.utils.OpenPhonicsRequestValidator
import com.openphonics.utils.OpenPhonicsRequestValidatorImpl
import dagger.Binds
import javax.inject.Singleton
import dagger.Module
@Module
interface ValidatorModule {
    @Singleton
    @Binds
    fun validator(validator: OpenPhonicsRequestValidatorImpl): OpenPhonicsRequestValidator
}