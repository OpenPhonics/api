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

package com.openphonics.exception

object FailureMessages {
    const val MESSAGE_MISSING_CREDENTIALS = "Required 'name' or 'classCode' missing."
    const val MESSAGE_MISSING_NOTE_DETAILS = "Required 'title' or 'note' missing."
    const val MESSAGE_MISSING_DEPTH = "Required depth"
    const val MESSAGE_MISSING_DATA = "Message is missing request data"

    const val MESSAGE_ACCESS_DENIED = "Access Denied!"
    const val MESSAGE_FAILED = "Something went wrong!"

    const val MESSAGE_MISSING_PIN_DETAILS = "Required 'isPinned' missing."
}