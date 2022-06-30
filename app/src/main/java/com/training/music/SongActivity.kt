package com.training.music

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.training.music.databinding.ActivitySongBinding
import com.training.music.dto.PlaylistDto
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
            var _ivPic = view.findViewById<ImageView>(R.id.ivPic)
            var _tvName = view.findViewById<TextView>(R.id.tvName)
            var _tvComposer = view.findViewById<TextView>(R.id.tvComposer)
            var _tvTrack = view.findViewById<TextView>(R.id.tvTrack)
            var _tvLike = view.findViewById<TextView>(R.id.tvLike)
            var _ivLike = view.findViewById<ImageView>(R.id.ivLike)
            var _actionEdit = view.findViewById<TextView>(R.id.actionEdit)
            var _ivAddList = view.findViewById<ImageView>(R.id.ivAddList)


            _tvName.text = item.name.uppercase()
            _tvComposer.text = item.composer
            _tvTrack.text = item.track.toString()
            _ivLike.setColorFilter(ContextCompat.getColor(this@SongActivity, R.color.app_action_normal), android.graphics.PorterDuff.Mode.SRC_IN)
            if(item.like.toString().equals("null")){
                _tvLike.text = "0"
            }else{
                _ivLike.setColorFilter(ContextCompat.getColor(this@SongActivity, R.color.app_action_hover), android.graphics.PorterDuff.Mode.SRC_IN)
                _tvLike.text = item.like.toString()
            }
            Picasso.get().load(item.image).into(_ivPic)
            _actionEdit.setOnClickListener{
                editElement(item)
            }
            _ivLike.setOnClickListener{
                onLike(item)
            }
            _ivAddList.setOnClickListener {
                addList(item)
            }
        }
    }

    private fun addList(item: SongDto) {
        var intent = Intent(this@SongActivity, AddsongActivity::class.java)
        intent.putExtra("song", item)
        this.startActivity(intent)
        true
    }

    private fun onLike(_song: SongDto) {
        CoroutineScope(Dispatchers.IO).launch {
            val _res: Response<*> = ApiObject.getRetro().songLike(_song._id)
            runOnUiThread{
                if(_res.isSuccessful){
                    Log.d("TAG", _song._id.toString())
                    list()
                    //adapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@SongActivity, "ERROR DE CONEXION", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun editElement(_obj: SongDto) {
        var intent = Intent(this@SongActivity, CRUDSongActivity::class.java)
        intent.putExtra("song", _obj)
        this.startActivity(intent)
        true
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
        binding.ivBack.setOnClickListener {
            onBackPressed()
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
        binding.progress.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            val _res: Response<Song>
            _res = ApiObject.getRetro().listSong()
            val _response = _res.body()!!
            val _list = _response.data
            runOnUiThread{
                if(_res.isSuccessful){
                    _songList.clear()
                    _songList.addAll(_list)
                    binding.progress.visibility = View.GONE
                    adapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@SongActivity, "ERROR DE CONEXION", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}