/*
 * Copyright (C) 2020 Sandip Vaghela
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

package com.afterroot.watchdone.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.afterroot.tmdbapi.model.MovieDb
import com.afterroot.tmdbapi.model.core.MovieResultsPage
import com.afterroot.tmdbapi2.model.RequestBodyToken
import com.afterroot.tmdbapi2.repository.AuthRepository
import com.afterroot.tmdbapi2.repository.GenresRepository
import com.afterroot.tmdbapi2.repository.MoviesRepository
import com.afterroot.watchdone.database.DatabaseFields
import com.afterroot.watchdone.database.MyDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.inject

class HomeViewModel(val savedState: SavedStateHandle) : ViewModel(), KoinComponent {
    private val db: FirebaseFirestore by inject()
    private var trendingMovies = MutableLiveData<MovieResultsPage>()
    private var watchlistSnapshot = MutableLiveData<ViewModelState>()
    val error = MutableLiveData<String?>()
    val selectedMovie = MutableLiveData<MovieDb>()

    fun getWatchlistSnapshot(userId: String): LiveData<ViewModelState> {
        watchlistSnapshot.apply {
            if (value == null) {
                value = ViewModelState.Loading
                db.collection(DatabaseFields.COLLECTION_USERS).document(userId)
                    .collection(DatabaseFields.COLLECTION_WATCHDONE)
                    .addSnapshotListener { querySnapshot, _ ->
                        querySnapshot.let { value = ViewModelState.Loaded(it) }
                    }
            }
        }
        return watchlistSnapshot
    }

    fun getTrendingMovies(isReload: Boolean = false): LiveData<MovieResultsPage> {
        if (trendingMovies.value == null || isReload) {
            trendingMovies = liveData(Dispatchers.IO) {
                try {
                    emit(get<MoviesRepository>().getMoviesTrendingInSearch())
                } catch (e: Exception) {
                    error.value = e.message
                    Log.e("TMDbApi", "getTrendingMovies: ${e.message}")
                }
            } as MutableLiveData<MovieResultsPage>
        }
        return trendingMovies
    }

    /**
     * For sending data to other fragment
     */
    fun selectMovie(movie: MovieDb) {
        selectedMovie.value = movie
    }

    fun getResponseRequestToken() = liveData(Dispatchers.IO) {
        emit(
            get<AuthRepository>().createRequestToken(RequestBodyToken("https://afterroot.web.app/apps/watchdone"))
        )
    }

    fun addGenres() = viewModelScope.launch {
        val genres = get<GenresRepository>().getMoviesGenres().genres
        get<MyDatabase>().genreDao().add(genres)
    }
}