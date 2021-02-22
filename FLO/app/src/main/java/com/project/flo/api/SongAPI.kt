package com.project.flo.api

import com.project.flo.model.Song
import retrofit2.Response
import retrofit2.http.GET

interface SongAPI {
    @GET("/2020-flo/song.json")
    suspend fun getSong(
    ): Response<Song>
}