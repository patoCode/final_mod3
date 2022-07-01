package com.training.music

import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.music.databinding.ActivityPlayerBinding
import com.training.music.dto.AlbumDto
import com.training.music.dto.PlaylistDto
import com.training.music.dto.SongDto
import com.training.music.generics.GenericAdapter
import com.training.music.generics.GenericBinding
import com.training.music.network.ApiObject
import com.training.music.response.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class PlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivityPlayerBinding
    private val _songList = mutableListOf<SongDto>()
    private lateinit var adapter : GenericAdapter<SongDto>
    private var _element: PlaylistDto? = null
    lateinit var player:MediaPlayer
    private val bindingInterface = object : GenericBinding<SongDto> {
        override fun bindData(item:SongDto, view: View){
            var _ivPic = view.findViewById<ImageView>(R.id.ivPic)
            var _tvName = view.findViewById<TextView>(R.id.tvName)
            var _tvComposer = view.findViewById<TextView>(R.id.tvComposer)

            var _btnPlay = view.findViewById<ImageView>(R.id.btnPlay)
            var _btnPause= view.findViewById<ImageView>(R.id.btnPause)
            _btnPlay.visibility = View.VISIBLE
            _btnPause.visibility = View.GONE

            _tvName.text = item.name.uppercase()
            _tvComposer.text = item.composer

            _btnPlay.setOnClickListener {
                playSong(item)
                _btnPlay.visibility = View.GONE
                _btnPause.visibility = View.VISIBLE
            }
            _btnPause.setOnClickListener {
                pauseSong()
                _btnPause.visibility = View.GONE
                _btnPlay.visibility = View.VISIBLE
            }
        }
    }
    private fun playSong(item: SongDto) {
        val url = "https://p.scdn.co/mp3-preview/cdeb69b04b6bd105406677668868f0b4be51ce8b?cid=774b29d4f13844c495f206cafdad9c86"//item.url
        player = MediaPlayer()
        player!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try{
            player!!.setDataSource(url)
            player!!.prepare()
            player!!.start()
        }
        catch(e: IOException){
            e.printStackTrace()
        }
    }
    private fun pauseSong(){
        if(player!!.isPlaying){
            player!!.stop()
            player!!.reset()
            player!!.release()
        }else{
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent.extras != null){
            _element = intent.extras!!.getSerializable("playlist") as PlaylistDto?
        }
        initRecycler()
        list()
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

    }
    private fun initRecycler(){
        adapter = GenericAdapter<SongDto>(_songList, R.layout.play_song, bindingInterface)
        binding.rvSong.layoutManager = LinearLayoutManager(this)
        binding.rvSong.adapter = adapter
    }
    fun list(){
       // binding.progress.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            val _res: Response<Song> = ApiObject.getRetro().playList(_element!!._id.toString())
            val _response = _res.body()!!
            val _list = _response.data

            Log.d("TAG", _list.toString())
            runOnUiThread{
                if(_res.isSuccessful){
                    _songList.clear()
                    _songList.addAll(_list)
                   // binding.progress.visibility = View.GONE
                    adapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@PlayerActivity, "ERROR DE CONEXION", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}