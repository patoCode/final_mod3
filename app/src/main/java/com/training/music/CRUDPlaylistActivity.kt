package com.training.music

import android.icu.number.IntegerWidth
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.training.music.databinding.ActivityCrudplaylistBinding
import com.training.music.dto.AlbumDto
import com.training.music.dto.PlaylistDto
import com.training.music.network.ApiObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class CRUDPlaylistActivity : AppCompatActivity() {
    lateinit var binding : ActivityCrudplaylistBinding
    lateinit var arrayAdapter: ArrayAdapter<*>
    private var _element: PlaylistDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrudplaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.extras != null){
            _element = intent.extras!!.getSerializable("playlist") as PlaylistDto?
            binding.etName.setText(_element?.name.toString())
            binding.etPriority.setText(_element?.priority.toString())
        }
        binding.btnSave.setOnClickListener {
            onClickSave()
        }
    }

    private fun onClickSave() {
        if(_element == null){
            _element = PlaylistDto(
                null,
                binding.etName.text.toString(),
                Integer.parseInt(binding.etPriority.text.toString()),
                null
            )
            CoroutineScope(Dispatchers.IO).launch {
                val response: Response<*> = ApiObject.getRetro().addPlaylist(_element!!)
                Log.d("TAGA", response.toString())
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CRUDPlaylistActivity,"Guardado correctamente", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }
            }
        }else{
            _element!!.name = binding.etName.toString()
            _element!!.priority = Integer.parseInt(binding.etPriority.toString())
            _element!!.cancion = null
            Log.d("AAA", _element!!.toString())
            CoroutineScope(Dispatchers.IO).launch {
                val response: Response<*> = ApiObject.getRetro().updatePlaylist(_element!!._id, _element!!)
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CRUDPlaylistActivity,"Editado correctamente",Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }
            }
            Toast.makeText(this@CRUDPlaylistActivity,"Editado correctamente", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }
}