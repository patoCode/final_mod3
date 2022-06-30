package com.training.music.dto

import com.training.music.generics.GenericDto
import java.io.Serializable

data class PlaylistDto(
    var _id: String?,
    var name :String,
    var priority: Int,
    var cancion: MutableList<*>?
): Serializable, GenericDto{
    override fun toString(): String {
        return name
    }
}