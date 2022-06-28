package com.training.music.response

import com.google.gson.annotations.SerializedName
import com.training.music.dto.AlbumDto
import com.training.music.dto.ArtistDto
import java.io.Serializable

data class Album(
    @SerializedName("status") var status: String,
    @SerializedName("albums") var data: List<AlbumDto>
):Serializable