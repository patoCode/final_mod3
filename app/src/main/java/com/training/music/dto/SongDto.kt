package com.training.music.dto

import com.training.music.generics.GenericDto
import com.training.music.response.Playlist
import java.io.Serializable

data class SongDto(
    var _id: String?,
    var name: String,
    var image: String?,
    var track: Int,
    var composer: String,
    var url: String,
    var lyrics: String,
    var like: Int? = 0,
    var playlist: MutableList<*>?,
    var album: String?
):Serializable, GenericDto {
    override fun toString(): String{
        return name
    }
}