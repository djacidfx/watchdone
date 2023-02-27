/*
 * Copyright (C) 2020-2023 Sandip Vaghela
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.afterroot.watchdone.data.mapper

import com.afterroot.data.model.NetworkUser
import com.afterroot.watchdone.data.model.LocalUser

fun LocalUser.toNetworkUser(): NetworkUser = NetworkUser(
    name = name,
    email = email,
    uid = uid,
    fcmId = fcmId,
    userName = userName,
    properties = properties
)

fun NetworkUser.toLocalUser(): LocalUser = LocalUser(
    name = name,
    email = email,
    uid = uid,
    fcmId = fcmId,
    userName = userName,
    properties = properties
)
