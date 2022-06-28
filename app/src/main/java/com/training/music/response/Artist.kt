package com.training.music.response

import com.google.gson.annotations.SerializedName
import com.training.music.dto.ArtistDto
import java.io.Serializable

data class Artist(
    @SerializedName("status") var status:String,
    @SerializedName("artists") var data:List<ArtistDto>
):Serializable