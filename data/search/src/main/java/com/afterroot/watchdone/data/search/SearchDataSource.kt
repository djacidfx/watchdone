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

package com.afterroot.watchdone.data.search

import com.afterroot.watchdone.data.model.Media
import com.afterroot.watchdone.data.model.MediaType

interface SearchDataSource {
  suspend fun search(params: Params): List<Media>

  data class Params(val mediaType: MediaType, val query: String, val page: Int = 1)
}
