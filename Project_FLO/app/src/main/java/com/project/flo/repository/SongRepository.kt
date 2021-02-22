package com.project.flo.repository

import com.project.flo.api.RetrofitInstance

class SongRepository {
    suspend fun getSong() =
        RetrofitInstance.api.getSong()
}