/*
 * Copyright (C) 2020-2024 Sandip Vaghela
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

package com.afterroot.watchdone.data.tmdb.auth

import javax.inject.Inject

class WatchdoneAuthStore @Inject constructor(
  private val preferencesAuthStore: PreferencesAuthStore,
  private val blockStoreAuthStore: BlockStoreAuthStore,
) : AuthStore {
  private var lastAuthState: AuthState? = null

  override suspend fun get(): AuthState? {
    if (lastAuthState != null) {
      return lastAuthState
    }

    val prefResult: AuthState? = preferencesAuthStore.get()

    if (!blockStoreAuthStore.isAvailable()) {
      // Block Store isn't available, moving on...
      lastAuthState = prefResult
      return prefResult
    }

    @Suppress("IfThenToElvis")
    return if (prefResult == null) {
      // If we don't have a pref result, try Block Store and save it to preferences
      blockStoreAuthStore.get()?.also { preferencesAuthStore.save(it) }
    } else {
      // If we have a pref result, save it to Block Store
      prefResult.also { blockStoreAuthStore.save(it) }
    }.also {
      lastAuthState = it
    }
  }

  override suspend fun save(authState: AuthState) {
    preferencesAuthStore.save(authState)

    if (blockStoreAuthStore.isAvailable()) {
      blockStoreAuthStore.save(authState)
    }

    lastAuthState = authState
  }

  override suspend fun clear() {
    preferencesAuthStore.clear()

    if (blockStoreAuthStore.isAvailable()) {
      blockStoreAuthStore.clear()
    }

    lastAuthState = null
  }
}
