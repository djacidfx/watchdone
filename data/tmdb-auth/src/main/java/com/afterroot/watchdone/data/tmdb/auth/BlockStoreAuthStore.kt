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

import android.app.Application
import com.google.android.gms.auth.blockstore.Blockstore
import com.google.android.gms.auth.blockstore.BlockstoreClient
import com.google.android.gms.auth.blockstore.RetrieveBytesRequest
import com.google.android.gms.auth.blockstore.StoreBytesData
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.AvailabilityException
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class BlockStoreAuthStore @Inject constructor(private val context: Application) : AuthStore {
  private val blockStore by lazy { Blockstore.getClient(context) }

  override suspend fun get(): AuthState? = try {
    Result.success(
      get(TMDB_AUTH_KEY) ?: get(BlockstoreClient.DEFAULT_BYTES_DATA_KEY)?.also {
        clear()
        save(it)
      },
    )
  } catch (e: Throwable) {
    Result.failure(e)
  }.getOrNull()

  override suspend fun save(authState: AuthState) {
    save(TMDB_AUTH_KEY, authState.serializeToJson().encodeToByteArray())
  }

  override suspend fun clear() {
    val zero = ByteArray(0)
    save(TMDB_AUTH_KEY, zero)
    save(BlockstoreClient.DEFAULT_BYTES_DATA_KEY, zero)
  }

  override suspend fun isAvailable(): Boolean = with(GoogleApiAvailability.getInstance()) {
    if (isGooglePlayServicesAvailable(context) != ConnectionResult.SUCCESS) {
      // If Play Services isn't available, Block Store definitely isn't available
      return false
    }

    try {
      checkApiAvailability(blockStore).await()
      return true
    } catch (e: AvailabilityException) {
      // Block Store isn't available so return false
      return false
    }
  }

  private suspend fun get(key: String): AuthState? {
    val response = blockStore.retrieveBytes(
      RetrieveBytesRequest.Builder()
        .setKeys(listOf(key))
        .build(),
    ).await()

    return response.blockstoreDataMap[key]
      ?.bytes
      ?.decodeToString()
      ?.let(::TmdbAuthStateWrapper)
  }

  private suspend fun save(key: String, bytes: ByteArray) {
    val data = StoreBytesData.Builder()
      .setShouldBackupToCloud(false)
      .setKey(key)
      .setBytes(bytes)
      .build()

    blockStore.storeBytes(data).await()
  }

  private companion object {
    const val TMDB_AUTH_KEY = "com.afterroot.watchdone.blockstore.tmdb_auth"
  }
}
