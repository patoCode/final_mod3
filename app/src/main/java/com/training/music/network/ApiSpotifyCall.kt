package com.training.music.network

import com.training.music.response.Artist
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiSpotifyCall {

    @Headers(
        "Content-Type: application/json",
        "Host: api.spotify.com",
        "Authorization:  Bearer "+ TOKEN_SP
    )
    @GET("v1/search")
    suspend fun searchTrack(
            @Query("q") q : String,
            @Query("type") type : String,
            @Query("market") market : String? = "ES",
            @Query("limit") limit : Int? = 10,
            @Query("offset") offset : Int? = 0,
    ) : Response<*>
}