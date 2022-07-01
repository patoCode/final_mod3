package com.training.music.dto

import java.io.Serializable

class SpotifySong(
    var id : String,
    var durationMillis : Int,
    var explicit : Boolean,
    var hrefOpen : String,
    var nameSong : String,
    var popularity : Int,
    var previewUrl : String,
    var tackNumber : Int,
):Serializable {
    override fun toString(): String {
        return nameSong
    }
}