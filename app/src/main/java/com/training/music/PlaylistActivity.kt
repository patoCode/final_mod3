package com.training.music

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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

    private val bindingInterface = object : GenericBinding<PlaylistDto> {
        override fun bindData(item:PlaylistDto, view: View){
            var _tvName = view.findViewById<TextView>(R.id.tvName)
            var _actionEdit = view.findViewById<TextView>(R.id.actionEdit)
            _tvName.text = item.name.uppercase()
            _actionEdit.setOnClickListener{
                editElement(item)
            }
        }
    }

    private fun editElement(_obj: PlaylistDto) {
        var intent = Intent(this@PlaylistActivity, CRUDPlaylistActivity::class.java)
        intent.putExtra("playlist", _obj)
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