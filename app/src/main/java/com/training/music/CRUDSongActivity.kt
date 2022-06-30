package com.training.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.training.music.databinding.ActivityCrudsongBinding
import com.training.music.dto.AlbumDto
import com.training.music.dto.SongDto
import com.training.music.network.ApiObject
import com.training.music.response.Album
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class CRUDSongActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    lateinit var binding : ActivityCrudsongBinding
    private var _element: SongDto? = null

    /* SPINNER */
    lateinit var arrayAdapter: ArrayAdapter<*>
    private var _listAlbum: MutableList<AlbumDto> = ArrayList()
    private lateinit var _spinnerId : String
    /* END SPINNER */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrudsongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _fillSpinner()
        arrayAdapter = ArrayAdapter(this, R.layout.item_selection, _listAlbum)
        arrayAdapter.setDropDownViewResource(R.layout.item_selection)

        with(binding.matAlbum) {
            setAdapter(arrayAdapter)
            onItemClickListener = this@CRUDSongActivity
        }
        binding.tilAlbum.isVisible = true
        if(intent.extras != null){
            _element = intent.extras!!.getSerializable("song") as SongDto?
            binding.tilAlbum.isVisible = false
            binding.etName.setText(_element?.name)
            binding.etImage.setText(_element?.image)
            binding.etTrack.setText(_element?.track.toString())
            binding.etComposer.setText(_element?.composer)
            binding.etUrl.setText(_element?.url)
            binding.etLyrics.setText(_element?.lyrics)
        }
        binding.btnSave.setOnClickListener {
            onClickSave()
        }

        binding.ivBack.setOnClickListener{
            onBackPressed()
        }
    }

    private fun _fillSpinner() {
        CoroutineScope(Dispatchers.IO).launch {
            val _res: Response<Album>
            _res = ApiObject.getRetro().listAlbum()
            val _response = _res.body()!!
            val _list = _response.data
            Log.d("SP", _list.toString())
            runOnUiThread{
                if(_res.isSuccessful){
                    _listAlbum.addAll(_list)
                }else{
                    Toast.makeText(this@CRUDSongActivity, "ERROR DE CONEXION", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position) as AlbumDto
        _spinnerId = item._id.toString()
    }

    private fun onClickSave() {
        if(_element == null){
            var image = "https://via.placeholder.com/100"
            if(!binding.etImage.text.toString().isEmpty()){
                image = binding.etImage.text.toString()
            }
            _element = SongDto(
                null,
                binding.etName.text.toString(),
                image,
                binding.etTrack.text.toString().toInt(),
                binding.etComposer.text.toString(),
                binding.etUrl.text.toString(),
                binding.etLyrics.text.toString(),
                null,
                null,
                _spinnerId
            )
            CoroutineScope(Dispatchers.IO).launch {
                val response: Response<*> = ApiObject.getRetro().addSong(_element!!)
                Log.d("TAGA", response.toString())
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CRUDSongActivity,"Guardado correctamente", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }
            }
        }else{
            _element!!.name = binding.etName.text.toString()
            _element!!.image = binding.etImage.text.toString()
            _element!!.track = binding.etTrack.text.toString().toInt()
            _element!!.composer = binding.etComposer.text.toString()
            _element!!.url = binding.etUrl.text.toString()
            _element!!.lyrics = binding.etLyrics.text.toString()
            _element!!.like = _element!!.like
            _element!!.playlist = null
            _element!!.album = _element!!.album

            CoroutineScope(Dispatchers.IO).launch {
                val response: Response<*> = ApiObject.getRetro().updateSong(_element!!._id, _element!!)
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CRUDSongActivity,"Guardado correctamente", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }
            }
        }
    }
}