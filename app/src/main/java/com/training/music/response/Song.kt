package com.training.music.response

import com.google.gson.annotations.SerializedName
import com.training.music.dto.SongDto
import java.io.Serializable

data class Song(
    @SerializedName("status") var status:String,
    @SerializedName("songs") var data:List<SongDto>
):Serializable