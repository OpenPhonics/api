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

package com.openphonics.di.component

import dagger.Lazy
import dagger.Subcomponent
import com.openphonics.auth.JWTController
import com.openphonics.controller.AuthController
import com.openphonics.controller.DataController
import com.openphonics.controller.ProgressController
import com.openphonics.di.module.ConfigModule
import com.openphonics.di.module.ControllerModule
import com.openphonics.di.module.DaoModule
import com.openphonics.di.module.EncryptorModule
import io.ktor.util.KtorExperimentalAPI
import javax.inject.Singleton

@KtorExperimentalAPI
@Singleton
@Subcomponent(modules = [ConfigModule::class, EncryptorModule::class, ControllerModule::class, DaoModule::class])
interface ControllerComponent {
    fun authController(): Lazy<AuthController>

    fun progressController(): Lazy<ProgressController>

    fun dataController(): Lazy<DataController>
    fun jwtController(): Lazy<JWTController>
}