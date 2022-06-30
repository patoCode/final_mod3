package com.training.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.training.music.databinding.ActivityAddsongBinding
import com.training.music.dto.AlbumDto
import com.training.music.dto.PlaylistDto
import com.training.music.dto.SongDto
import com.training.music.dto.SongListDto
import com.training.music.network.ApiObject
import com.training.music.response.Album
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class AddsongActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    lateinit var binding:ActivityAddsongBinding
    private var _element: SongDto? = null

    /* SPINNER */
    lateinit var arrayAdapter: ArrayAdapter<*>
    private var _listPlay: MutableList<PlaylistDto> = ArrayList()
    private lateinit var _spinnerId : String
    /* END SPINNER */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddsongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _fillSpinner()
        arrayAdapter = ArrayAdapter(this, R.layout.item_selection, _listPlay)
        arrayAdapter.setDropDownViewResource(R.layout.item_selection)

        with(binding.matPlay) {
            setAdapter(arrayAdapter)
            onItemClickListener = this@AddsongActivity
        }

        if(intent.extras != null){
            _element = intent.extras!!.getSerializable("song") as SongDto?
        }

        binding.btnSave.setOnClickListener {
            onClickSave()
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun _fillSpinner() {
        CoroutineScope(Dispatchers.IO).launch {
            val _res = ApiObject.getRetro().listPlaylist()
            val _response = _res.body()!!
            val _list = _response.data
            Log.d("SP", _list.toString())
            runOnUiThread{
                if(_res.isSuccessful){
                    _listPlay.addAll(_list)
                }else{
                    Toast.makeText(this@AddsongActivity, "ERROR DE CONEXION", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position) as PlaylistDto
        _spinnerId = item._id.toString()
    }

    private fun onClickSave() {
        if(_element != null){
            var data= SongListDto(
                _element!!._id.toString(),
                _spinnerId
            )
            CoroutineScope(Dispatchers.IO).launch {
                val response: Response<*> = ApiObject.getRetro().addSongList(data)
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddsongActivity,"Guardado correctamente", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }
            }
        }
    }
}