package com.training.music

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.training.music.databinding.ActivityMainBinding
import com.training.music.response.Playlist

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun actionCancion(view: View) {
        val intent = Intent(this, SongActivity::class.java).apply {}
        startActivity(intent)
    }
    fun actionArtista(view: View) {
        val intent = Intent(this, ArtistActivity::class.java).apply {}
        startActivity(intent)
    }

    fun actionAlbum(view: View) {
        val intent = Intent(this, AlbumActivity::class.java).apply {}
        startActivity(intent)
    }

    fun actionPlaylist(view: View) {
        val intent = Intent(this, PlaylistActivity::class.java).apply {}
        startActivity(intent)
    }


}