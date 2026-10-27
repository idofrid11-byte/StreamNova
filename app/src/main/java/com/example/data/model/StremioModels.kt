package com.example.data.model

import com.google.gson.annotations.SerializedName

data class StremioManifest(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("version") val version: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("types") val types: List<String> = emptyList(),
    @SerializedName("catalogs") val catalogs: List<CatalogDescriptor> = emptyList(),
    @SerializedName("resources") val resources: List<Any>? = emptyList(),
    @SerializedName("logo") val logo: String? = null,
    @SerializedName("background") val background: String? = null
)

data class CatalogDescriptor(
    @SerializedName("type") val type: String,
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("extra") val extra: List<CatalogExtra>? = null
)

data class CatalogExtra(
    @SerializedName("name") val name: String,
    @SerializedName("isRequired") val isRequired: Boolean = false,
    @SerializedName("options") val options: List<String>? = null
)

data class CatalogResponse(
    @SerializedName("metas") val metas: List<MetaItem> = emptyList()
)

data class MetaResponse(
    @SerializedName("meta") val meta: MetaItem? = null
)

data class MetaItem(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String = "movie",
    @SerializedName("name") val name: String = "",
    @SerializedName("poster") val poster: String? = null,
    @SerializedName("background") val background: String? = null,
    @SerializedName("logo") val logo: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("releaseInfo") val releaseInfo: String? = null,
    @SerializedName("imdbRating") val imdbRating: String? = null,
    @SerializedName("genres") val genres: List<String>? = emptyList(),
    @SerializedName("cast") val cast: List<String>? = emptyList(),
    @SerializedName("director") val director: List<String>? = emptyList(),
    @SerializedName("runtime") val runtime: String? = null,
    @SerializedName("videos") val videos: List<VideoEpisode>? = null
)

data class VideoEpisode(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String = "",
    @SerializedName("season") val season: Int? = 1,
    @SerializedName("episode") val episode: Int? = 1,
    @SerializedName("released") val released: String? = null,
    @SerializedName("thumbnail") val thumbnail: String? = null,
    @SerializedName("overview") val overview: String? = null
)

data class StreamResponse(
    @SerializedName("streams") val streams: List<StreamItem> = emptyList()
)

data class StreamItem(
    @SerializedName("title") val title: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("infoHash") val infoHash: String? = null,
    @SerializedName("fileIdx") val fileIdx: Int? = null,
    @SerializedName("behaviorHints") val behaviorHints: BehaviorHints? = null,
    val quality: String = "1080p",
    val provider: String = "Direct Stream",
    val isDirectPlayable: Boolean = true
)

data class BehaviorHints(
    @SerializedName("notWebReady") val notWebReady: Boolean? = false,
    @SerializedName("bingeGroup") val bingeGroup: String? = null,
    @SerializedName("proxyHeaders") val proxyHeaders: Map<String, String>? = null
)
