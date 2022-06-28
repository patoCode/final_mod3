package com.training.music.dto

import com.training.music.generics.GenericDto
import java.io.Serializable

data class ArtistDto(
    var _id: String?,
    var name :String,
    var alias : String
) : Serializable, GenericDto {
    override fun toString(): String{
        return name
    }
}