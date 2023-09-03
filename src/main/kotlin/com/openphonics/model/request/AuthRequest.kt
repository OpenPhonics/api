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

package com.openphonics.model.request

import kotlinx.serialization.Serializable

/**
 * Model for getting API request body parameters for authentication.
 */
@Serializable
data class SignUpRequest(
    val name: String,
    val classCode: String,
    val native: String,
    val language: Int,
    val isAdmin: Boolean,
)
@Serializable
data class LoginRequest(
    val name: String,
    val classCode: String,
)