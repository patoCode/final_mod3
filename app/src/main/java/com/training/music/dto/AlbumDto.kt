package com.training.music.dto

import com.training.music.generics.GenericDto
import java.io.Serializable

data class AlbumDto(
    var _id: String?,
    var name: String,
    var cover: String,
    var year: Int,
    var cancion: MutableList<*>?,
    var artista: String
):Serializable, GenericDto{
    override fun toString(): String{
        return name
    }
}