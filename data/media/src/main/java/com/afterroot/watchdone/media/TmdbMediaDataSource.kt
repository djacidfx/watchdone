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

package com.afterroot.watchdone.media

import app.moviebase.tmdb.Tmdb3
import com.afterroot.watchdone.data.mapper.toMedia
import com.afterroot.watchdone.data.model.Media
import com.afterroot.watchdone.data.model.MediaType
import javax.inject.Inject

class TmdbMediaDataSource @Inject constructor(
    private val tmdb: Tmdb3,
) : MediaDataSource {
    override suspend fun getMedia(media: Media): Media {
        val tmdbId = media.tmdbId
            ?: throw IllegalArgumentException("TmdbId for movie/show does not exist [$media]")

        val result = when (media.mediaType) {
            MediaType.MOVIE -> {
                tmdb.movies.getDetails(tmdbId).toMedia()
            }

            MediaType.SHOW -> {
                tmdb.show.getDetails(tmdbId).toMedia()
            }

            else -> {
                throw IllegalArgumentException("MediaType not supported")
            }
        }

        return result
    }
}