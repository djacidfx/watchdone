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

package com.afterroot.watchdone.adapter

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.afterroot.core.extensions.getDrawableExt
import com.afterroot.core.extensions.inflate
import com.afterroot.tmdbapi.model.MovieDb
import com.afterroot.tmdbapi.model.core.AbstractJsonMapping
import com.afterroot.watchdone.GlideApp
import com.afterroot.watchdone.R
import com.afterroot.watchdone.ui.settings.Settings
import kotlinx.android.synthetic.main.list_item_movie.view.*
import org.koin.core.Koin

class MovieAdapterType(val callbacks: ItemSelectedCallback<MovieDb>, koin: Koin) : AdapterType {
    val settings = koin.get<Settings>()
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = MovieVH(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: AbstractJsonMapping) {
        holder as MovieVH
        holder.bind(item as MovieDb)
    }

    inner class MovieVH(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.list_item_movie)) {
        private val titleView: AppCompatTextView = itemView.title
        private val yearView: AppCompatTextView = itemView.year
        private val posterView: AppCompatImageView = itemView.poster
        private val rating = itemView.rating
        fun bind(item: MovieDb) {
            titleView.text = item.title
            yearView.text = item.releaseDate
            rating.text = item.voteAverage.toString()
            if (item.posterPath != null) {
                GlideApp.with(posterView.context).load(settings.baseUrl + settings.imageSize + item.posterPath)
                    .into(posterView)
            } else GlideApp.with(posterView.context).load(posterView.context.getDrawableExt(R.drawable.ic_broken_image))
                .override(posterView.context.resources.getDimensionPixelSize(R.dimen.placeholder_image_size))
                .into(posterView)
            with(super.itemView) {
                tag = item
                setOnClickListener {
                    callbacks.onClick(bindingAdapterPosition, itemView, item)
                }
                setOnLongClickListener {
                    callbacks.onLongClick(bindingAdapterPosition, item)
                    return@setOnLongClickListener true
                }
            }
        }
    }
}