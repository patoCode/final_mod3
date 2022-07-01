package com.training.music.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val URL_SP = "https://api.spotify.com/"
const val TOKEN_SP = "BQD7A6u_njM9-stdLnV0I30MY8AbeE0LV15rKcLViYDmYwpiOSy3ZmSsZnVmcNEJ0PWVVWVGmfIj0q9SuPt94Wq3yszXCT5nail71I3IG2hCvCDtUbCON6Po_37vrp22SqnoetIlnYtTJRsWom0EXBPcjAkMUTrBFcIuhZmIeQ"
object ApiSpotify {
    fun getRetro():ApiSpotifyCall{
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(URL_SP).addConverterFactory(GsonConverterFactory.create(gson)).build()
        return retrofit.create(ApiSpotifyCall::class.java)
    }
}