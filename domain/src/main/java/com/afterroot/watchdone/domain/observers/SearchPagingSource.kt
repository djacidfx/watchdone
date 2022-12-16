/*
 * Copyright (C) 2020-2022 Sandip Vaghela
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

package com.afterroot.watchdone.domain.observers

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.afterroot.tmdbapi.model.Query
import com.afterroot.watchdone.data.mapper.toMovies
import com.afterroot.watchdone.data.mapper.toTV
import com.afterroot.watchdone.data.model.Movie
import com.afterroot.watchdone.data.model.TV
import com.afterroot.watchdone.domain.interactors.SearchMovieInteractor
import com.afterroot.watchdone.domain.interactors.SearchTVInteractor
import com.afterroot.watchdone.utils.State
import timber.log.Timber

class SearchMoviePagingSource(private val query: Query, private val searchMovieInteractor: SearchMovieInteractor) :
    PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        try {
            var nextPage = params.key ?: 1
            Timber.d("load: Page $nextPage, Query: $query")
            val response = searchMovieInteractor.executeSync(SearchMovieInteractor.Params(query.page(nextPage)))
            var loadResult: LoadResult<Int, Movie>? = null

            if (!query.validateForSearch()) {
                loadResult = LoadResult.Error(IllegalArgumentException("Query is Empty"))
                return loadResult
            }

            response.collect {
                when (it) {
                    is State.Success -> {
                        nextPage = it.data.page + 1
                        Timber.d("load: Success, Next page: $nextPage")
                        loadResult = LoadResult.Page(
                            data = it.data.toMovies(),
                            prevKey = null,
                            nextKey = if (nextPage <= it.data.totalPages) nextPage else null
                        )
                    }

                    is State.Loading -> {
                        Timber.d("load: Loading Page $nextPage")
                    }

                    is State.Failed -> {
                        Timber.d("load: Failed loading page")
                    }
                }
            }
            return loadResult!!
        } catch (e: Exception) {
            Timber.e("load: Load Error")
            return LoadResult.Error(e)
        }
    }
}

class SearchTVPagingSource(private val query: Query, private val searchTVInteractor: SearchTVInteractor) :
    PagingSource<Int, TV>() {
    override fun getRefreshKey(state: PagingState<Int, TV>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TV> {
        try {
            var nextPage = params.key ?: 1
            Timber.d("load: Page $nextPage, Query: $query")
            val response = searchTVInteractor.executeSync(SearchTVInteractor.Params(query.page(nextPage)))
            var loadResult: LoadResult<Int, TV>? = null

            if (!query.validateForSearch()) {
                loadResult = LoadResult.Error(IllegalArgumentException("Query is Empty"))
                return loadResult
            }

            response.collect {
                when (it) {
                    is State.Success -> {
                        nextPage = it.data.page + 1
                        Timber.d("load: Success, Next page: $nextPage")
                        loadResult = LoadResult.Page(
                            data = it.data.toTV(),
                            prevKey = null,
                            nextKey = if (nextPage <= it.data.totalPages) nextPage else null
                        )
                    }

                    is State.Loading -> {
                        Timber.d("load: Loading Page $nextPage")
                    }

                    is State.Failed -> {
                        Timber.d("load: Failed loading page")
                    }
                }
            }
            return loadResult!!
        } catch (e: Exception) {
            Timber.e("load: Load Error")
            return LoadResult.Error(e)
        }
    }
}
