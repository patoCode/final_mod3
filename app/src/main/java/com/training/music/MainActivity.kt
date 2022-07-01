package com.training.music

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.training.music.databinding.ActivityMainBinding
import com.training.music.network.ApiObject
import com.training.music.network.ApiSpotify
import com.training.music.response.Playlist
import com.training.music.response.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bSpotify.setOnClickListener {
            getSpotify();
        }

    }

    private fun getSpotify() {
        CoroutineScope(Dispatchers.IO).launch {
            val _res: Response<*> = ApiSpotify.getRetro().searchTrack(
                "track: felices los 4",
                "track"
            )
            val _response = _res.body()!!
            Log.d("SPOTIFY", _response.tracks!!.items!!.toString())
            runOnUiThread{
                if(_res.isSuccessful){

                }else{
                    Toast.makeText(this@MainActivity, "ERROR DE CONEXION", Toast.LENGTH_SHORT).show()
                }
            }
        }
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