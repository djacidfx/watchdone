package com.afterroot.tmdbapi.model

import com.afterroot.tmdbapi.TmdbMovies.ReleaseInfoResults
import com.afterroot.tmdbapi.Types
import com.afterroot.tmdbapi.model.core.IdElement
import com.afterroot.tmdbapi.model.core.MovieKeywords
import com.afterroot.tmdbapi.model.core.ResultsPage
import com.afterroot.tmdbapi.model.keywords.Keyword
import com.afterroot.tmdbapi.model.people.PersonCast
import com.afterroot.tmdbapi.model.people.PersonCrew
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
class MovieDb : IdElement(), Multi {
    @JsonProperty("title")
    var title: String? = null

    @JsonProperty("original_title")
    var originalTitle: String? = null

    @JsonProperty("popularity")
    var popularity = 0f

    @JsonProperty("backdrop_path")
    var backdropPath: String? = null

    @JsonProperty("poster_path")
    var posterPath: String? = null

    @JsonProperty("release_date")
    var releaseDate: String? = null

    @JsonProperty("adult")
    var isAdult = false

    @JsonProperty("belongs_to_collection")
    var belongsToCollection: Collection? = null

    @JsonProperty("budget")
    var budget: Long = 0

    @JsonProperty("genres")
    var genres: List<Genre>? = null

    @JsonProperty("genre_ids")
    var genreIds: List<Int>? = null

    @JsonProperty("homepage")
    var homepage: String? = null

    @JsonProperty("overview")
    var overview: String? = null

    // todo still there??
    @JsonProperty("imdb_id")
    var imdbID: String? = null

    @JsonProperty("original_language")
    var originalLanguage: String? = null

    @JsonProperty("production_companies")
    var productionCompanies: List<ProductionCompany>? = null

    @JsonProperty("production_countries")
    var productionCountries: List<ProductionCountry>? = null

    @JsonProperty("revenue")
    var revenue: Long = 0

    @JsonProperty("runtime")
    var runtime = 0

    @JsonProperty("spoken_languages")
    var spokenLanguages: List<Language>? = null

    @JsonProperty("tagline")
    var tagline: String? = null

    @JsonProperty("rating")
    var userRating = 0f

    @JsonProperty("vote_average")
    var voteAverage = 0f

    @JsonProperty("vote_count")
    var voteCount = 0

    @JsonProperty("status")
    var status: String? = null

    // Appendable responses
    @JsonProperty("alternative_titles")
    private var alternativeTitles: MoviesAlternativeTitles? = null

    @JsonProperty("credits")
    var credits: Credits? = null

    @JsonProperty("images")
    private var images: MovieImages? = null

    // note: it seems to be a flaw in their api, because a paged result would be more consistent
    @JsonProperty("keywords")
    private var keywords: MovieKeywords? = null

    @JsonProperty("release_dates")
    private var releases: ReleaseInfoResults? = null

    @JsonProperty("videos")
    private var videos: Video.Results? = null

    @JsonProperty("translations")
    private var translations: MovieTranslations? = null

    @JsonProperty("similar")
    private var similarMovies: ResultsPage<MovieDb>? = null

    @JsonProperty("recommendations")
    private val recommendedMovies: ResultsPage<MovieDb>? = null

    @JsonProperty("reviews")
    private var reviews: ResultsPage<Reviews>? = null

    @JsonProperty("lists")
    private var lists: ResultsPage<MovieList>? = null

    fun getAlternativeTitles(): List<AlternativeTitle>? {
        return if (alternativeTitles != null) alternativeTitles!!.titles else null
    }

    val cast: List<PersonCast>?
        get() = if (credits != null) credits!!.getCast() else null

    val crew: List<PersonCrew>?
        get() = if (credits != null) credits!!.getCrew() else null

    fun getImages(vararg artworkTypes: ArtworkType?): List<Artwork>? {
        return if (images != null) images!!.getAll(*artworkTypes) else null
    }

    fun getKeywords(): List<Keyword>? {
        return if (keywords != null) keywords!!.keywords else null
    }

    fun getReleases(): List<ReleaseInfo>? {
        return if (releases != null) releases!!.results else null
    }

    fun getVideos(): List<Video>? {
        return if (videos != null) videos!!.videos else null
    }

    override fun toString(): String {
        return "$title - $releaseDate"
    }

    override fun getMediaType(): Multi.MediaType {
        return Multi.MediaType.MOVIE
    }

    fun getYear(): String? {
        return releaseDate?.substring(0, 4)
    }

    companion object {
        fun type(): Int {
            return Types.MOVIE
        }
    }
}