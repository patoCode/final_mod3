package com.training.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.training.music.databinding.ActivityCrudalbumBinding
import com.training.music.dto.AlbumDto
import com.training.music.dto.ArtistDto
import com.training.music.network.ApiObject
import com.training.music.response.Artist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class CRUDAlbumActivity : AppCompatActivity(), AdapterView.OnItemClickListener {
    lateinit var binding: ActivityCrudalbumBinding
    lateinit var arrayAdapter: ArrayAdapter<*>
    private var _element: AlbumDto? = null

    private var _listArtist: MutableList<ArtistDto> = ArrayList()
    private lateinit var _spinnerId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrudalbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _fillSpinner()
         arrayAdapter = ArrayAdapter(this, R.layout.item_selection, _listArtist)
        arrayAdapter.setDropDownViewResource(R.layout.item_selection)

        with(binding.matArtist) {
            setAdapter(arrayAdapter)
            onItemClickListener = this@CRUDAlbumActivity
        }

        if(intent.extras != null){
            _element = intent.extras!!.getSerializable("album") as AlbumDto?
        }
        binding.btnSave.setOnClickListener {
            onClickSave()
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val item = parent?.getItemAtPosition(position) as ArtistDto
        _spinnerId = item._id.toString()
    }

    private fun onClickSave() {
        if(_element == null){
            _element = AlbumDto(
                null,
                binding.etName.text.toString(),
                binding.etCover.text.toString(),
                Integer.parseInt(binding.etYear.text.toString()),
                null,
                _spinnerId
            )
            CoroutineScope(Dispatchers.IO).launch {
                val response: Response<*> = ApiObject.getRetro().addAlbum(_element!!)
                Log.d("TAGA", response.toString())
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CRUDAlbumActivity,"Guardado correctamente", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }
            }
        }
    }

    fun _fillSpinner(){
        CoroutineScope(Dispatchers.IO).launch {
            val _res: Response<Artist>
            _res = ApiObject.getRetro().listArtist()
            val _response = _res.body()!!
            val _list = _response.data
            Log.d("SP", _list.toString())
            runOnUiThread{
                if(_res.isSuccessful){
                    _listArtist.addAll(_list)
                }else{
                    Toast.makeText(this@CRUDAlbumActivity, "ERROR DE CONEXION", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}