package com.example.data.repository

import com.example.data.model.CatalogResponse
import com.example.data.model.MetaResponse
import com.example.data.model.StremioManifest
import com.example.data.model.StreamResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface StremioApiService {
    @GET
    suspend fun getManifest(@Url url: String): StremioManifest

    @GET
    suspend fun getCatalog(@Url url: String): CatalogResponse

    @GET
    suspend fun getMeta(@Url url: String): MetaResponse

    @GET
    suspend fun getStreams(@Url url: String): StreamResponse
}
