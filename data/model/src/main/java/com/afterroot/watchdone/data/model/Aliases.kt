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
package com.afterroot.watchdone.data.model

import app.moviebase.tmdb.model.TmdbEpisode
import app.moviebase.tmdb.model.TmdbEpisodeDetail
import app.moviebase.tmdb.model.TmdbKeywordDetail
import app.moviebase.tmdb.model.TmdbSeasonDetail
import app.moviebase.tmdb.model.TmdbVideo

typealias TmdbEpisodes = List<TmdbEpisode>?
typealias TmdbEpisodeDetails = List<TmdbEpisodeDetail>?

typealias Episodes = List<Episode>?

typealias TmdbSeasons = List<TmdbSeasonDetail>?
typealias Seasons = List<Season>?

typealias Keywords = List<TmdbKeywordDetail>?

typealias Videos = List<TmdbVideo>?
