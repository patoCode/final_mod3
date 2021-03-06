package com.training.music

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.music.databinding.ActivityPlaylistBinding
import com.training.music.dto.PlaylistDto
import com.training.music.generics.GenericAdapter
import com.training.music.generics.GenericBinding
import com.training.music.network.ApiObject
import com.training.music.response.Playlist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class PlaylistActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPlaylistBinding
    private lateinit var adapter : GenericAdapter<PlaylistDto>
    private val _list = mutableListOf<PlaylistDto>()
    var listCovers = listOf<Int>(
        R.drawable.pl01,
        R.drawable.pl02,
        R.drawable.pl03,
    )

    private val bindingInterface = object : GenericBinding<PlaylistDto> {
        override fun bindData(item:PlaylistDto, view: View){
            val _cover = (0..2).random()
            var _tvName = view.findViewById<TextView>(R.id.tvName)
            var _tvPriority = view.findViewById<TextView>(R.id.tvPriority)
            var _ivPic = view.findViewById<ImageView>(R.id.ivPic)
            var _actionEdit = view.findViewById<TextView>(R.id.actionEdit)
            var _actionPlay = view.findViewById<TextView>(R.id.actionPlay)

            _tvName.text = item.name.uppercase()
            _tvPriority.text = item.priority.let { it }.toString()
            _ivPic.setImageResource(listCovers[_cover])
            _actionEdit.setOnClickListener{
                editElement(item)
            }
            _actionPlay.setOnClickListener{
                goPlay(item)
            }
        }
    }

    private fun goPlay(item: PlaylistDto) {
        val intent = Intent(this, PlayerActivity::class.java).apply {}
        intent.putExtra("playlist", item)
        startActivity(intent)
    }

    private fun editElement(_obj: PlaylistDto) {
        var intent = Intent(this@PlaylistActivity, CRUDPlaylistActivity::class.java)
        intent.putExtra("playlist", _obj)
        intent.putExtra("name", _obj.name)
        this.startActivity(intent)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycler()
        list()
        binding.addElement.setOnClickListener {
            addElement()
        }
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        list()
    }
    private fun addElement() {
        var intent = Intent(this@PlaylistActivity, CRUDPlaylistActivity::class.java)
        startActivity(intent)
    }

    private fun initRecycler() {
        adapter = GenericAdapter<PlaylistDto>(_list, R.layout.playlist_item, bindingInterface)
        binding.rvPlaylist.layoutManager = LinearLayoutManager(this)
        binding.rvPlaylist.adapter = adapter
    }

    private fun list() {
        CoroutineScope(Dispatchers.IO).launch {
            val _res: Response<Playlist>
            _res = ApiObject.getRetro().listPlaylist()
            val _response = _res.body()!!
            val _playlist = _response.data
            Log.d("TAG", _playlist.toString())
            runOnUiThread{
                if(_res.isSuccessful){
                    _list.clear()
                    _list.addAll(_playlist)
                    adapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@PlaylistActivity, "ERROR DE CONEXION", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}