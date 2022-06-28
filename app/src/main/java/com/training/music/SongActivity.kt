package com.training.music

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.music.databinding.ActivitySongBinding
import com.training.music.dto.SongDto
import com.training.music.generics.GenericAdapter
import com.training.music.generics.GenericBinding
import com.training.music.network.ApiCall
import com.training.music.network.ApiObject
import com.training.music.response.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class SongActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySongBinding
    private lateinit var adapter : GenericAdapter<SongDto>
    private val _songList = mutableListOf<SongDto>()

    private val bindingInterface = object : GenericBinding<SongDto> {
        override fun bindData(item:SongDto, view:View){
            var _tvName = view.findViewById<TextView>(R.id.tvName)
            _tvName.text = item.name.uppercase()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
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
        var intent = Intent(this@SongActivity, CRUDSongActivity::class.java)
        startActivity(intent)
    }

    private fun initRecycler(){
        adapter = GenericAdapter<SongDto>(_songList, R.layout.song_item, bindingInterface)
        binding.rvSong.layoutManager = LinearLayoutManager(this)
        binding.rvSong.adapter = adapter
    }

    fun list(){
        CoroutineScope(Dispatchers.IO).launch {
            val _res: Response<Song>
            _res = ApiObject.getRetro().listSong()
            val _response = _res.body()!!
            val _list = _response.data
            runOnUiThread{
                if(_res.isSuccessful){
                    _songList.clear()
                    _songList.addAll(_list)
                    adapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@SongActivity, "ERROR DE CONEXION", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}