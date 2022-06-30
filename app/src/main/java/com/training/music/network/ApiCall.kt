package com.training.music.network

import com.training.music.dto.*
import com.training.music.response.Album
import com.training.music.response.Artist
import com.training.music.response.Playlist
import com.training.music.response.Song
import retrofit2.Response
import retrofit2.http.*

interface ApiCall {

    @GET("artista/list")
    suspend fun listArtist():Response<Artist>

    @GET("cancion/list")
    suspend fun listSong():Response<Song>

    @GET("album/list")
    suspend fun listAlbum():Response<Album>

    @GET("playlists/list")
    suspend fun listPlaylist():Response<Playlist>

    @GET("playlists/play/{id}")
    suspend fun playList(@Path("id") id :String?):Response<Song>

    @POST("artista/add")
    suspend fun addArtist(@Body artist: ArtistDto):Response<*>

    @POST("album/add")
    suspend fun addAlbum(@Body album: AlbumDto):Response<*>

    @POST("cancion/add")
    suspend fun addSong(@Body song: SongDto):Response<*>

    @POST("playlists/add")
    suspend fun addPlaylist(@Body playlist: PlaylistDto):Response<*>

    @PUT("cancion/edit/{id}")
    suspend fun updateSong(@Path("id") id :String?, @Body song: SongDto):Response<*>

    @PUT("artista/edit/{id}")
    suspend fun updateArtist(@Path("id") id :String?, @Body artist: ArtistDto):Response<*>

    @PUT("playlists/edit/{id}")
    suspend fun updatePlaylist(@Path("id") id :String?, @Body playlist: PlaylistDto):Response<*>

    @PUT("album/edit/{id}")
    suspend fun updateAlbum(@Path("id") id :String?, @Body album: AlbumDto):Response<*>

    @DELETE("artista/delete/{id}")
    suspend fun deleteArtist(@Path("id") id: String?): Response<*>

    @POST("cancion/like/{id}")
    suspend fun songLike(@Path("id") id: String?): Response<*>

    @POST("playlists/addsong")
    suspend fun addSongList(@Body song: SongListDto):Response<*>

}