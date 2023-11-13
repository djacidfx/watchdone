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

package com.afterroot.watchdone.ui.search

import androidx.compose.runtime.Immutable
import app.tivi.api.UiMessage
import com.afterroot.tmdbapi.model.Query
import com.afterroot.watchdone.base.compose.ViewState
import com.afterroot.watchdone.data.model.MediaType

@Immutable
data class SearchViewState(
    val mediaType: MediaType? = MediaType.MOVIE,
    val query: Query = Query(),
    val isLoading: Boolean = false,
    val refresh: Boolean = false,
    val empty: Boolean = true,
    override val message: UiMessage? = null,
) : ViewState() {
    companion object {
        val Empty = SearchViewState()
    }
}
