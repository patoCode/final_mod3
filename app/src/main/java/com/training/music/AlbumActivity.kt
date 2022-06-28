package com.training.music

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.music.databinding.ActivityAlbumBinding
import com.training.music.dto.AlbumDto
import com.training.music.dto.ArtistDto
import com.training.music.generics.GenericAdapter
import com.training.music.generics.GenericBinding
import com.training.music.network.ApiObject
import com.training.music.response.Album
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class AlbumActivity : AppCompatActivity() {

    lateinit var binding : ActivityAlbumBinding
    private lateinit var adapter : GenericAdapter<AlbumDto>
    private val _albumList = mutableListOf<AlbumDto>()
    private val bindingInterface = object : GenericBinding<AlbumDto> {
        override fun bindData(item:AlbumDto, view: View){
            var _tvName = view.findViewById<TextView>(R.id.tvName)
            var _actionEdit = view.findViewById<Button>(R.id.actionEdit)
            _tvName.text = item.name.uppercase()
            _actionEdit.setOnClickListener{
                editElement(item)
            }
        }
    }

    private fun editElement(_obj: AlbumDto){
        var intent = Intent(this@AlbumActivity, CRUDAlbumActivity::class.java)
        intent.putExtra("album", _obj)
        this.startActivity(intent)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumBinding.inflate(layoutInflater)
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
        var intent = Intent(this@AlbumActivity, CRUDAlbumActivity::class.java)
        startActivity(intent)
    }


    private fun initRecycler() {
        adapter = GenericAdapter<AlbumDto>(_albumList, R.layout.album_item, bindingInterface)
        binding.rvAlbum.layoutManager = LinearLayoutManager(this)
        binding.rvAlbum.adapter = adapter
    }

    private fun list() {
        CoroutineScope(Dispatchers.IO).launch {
            val _res: Response<Album>
            _res = ApiObject.getRetro().listAlbum()
            val _response = _res.body()!!
            val _list = _response.data
            Log.d("TAG", _list.toString())
            runOnUiThread{
                if(_res.isSuccessful){
                    _albumList.clear()
                    _albumList.addAll(_list)
                    adapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@AlbumActivity, "ERROR DE CONEXION", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}