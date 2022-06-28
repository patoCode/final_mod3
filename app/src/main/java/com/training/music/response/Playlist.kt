package com.training.music.response

import com.google.gson.annotations.SerializedName
import com.training.music.dto.AlbumDto
import com.training.music.dto.PlaylistDto
import java.io.Serializable

data class Playlist(
    @SerializedName("status") var status: String,
    @SerializedName("playlists") var data: List<PlaylistDto>
):Serializable