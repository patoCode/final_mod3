package com.training.music

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.training.music.databinding.ActivityCrudartistBinding
import com.training.music.dto.ArtistDto
import com.training.music.network.ApiObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class CRUDArtistActivity : AppCompatActivity() {

    lateinit var binding : ActivityCrudartistBinding
    private var _element: ArtistDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrudartistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent.extras != null){
            _element = intent.extras!!.getSerializable("artist") as ArtistDto?
            binding.etName.setText(_element?.name)
            binding.etAlias.setText(_element?.alias)
        }
        binding.btnSave.setOnClickListener {
            onClickSave()
        }
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun onClickSave() {
        if(_element == null){
            _element = ArtistDto(
                null,
                binding.etName.text.toString(),
                binding.etAlias.text.toString()
            )
            CoroutineScope(Dispatchers.IO).launch {
                val response: Response<*> = ApiObject.getRetro().addArtist(_element!!)
                Log.d("TAG", response.toString())
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CRUDArtistActivity,"Guardado correctamente", Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }
            }
        }else{
            _element!!.name = binding.etName.text.toString()
            _element!!.alias = binding.etAlias.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val response: Response<*> = ApiObject.getRetro().updateArtist(_element!!._id, _element!!)
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CRUDArtistActivity,"Editado correctamente",Toast.LENGTH_SHORT).show()
                        onBackPressed()
                    }
                }
            }
            Toast.makeText(this@CRUDArtistActivity,"Editado correctamente", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }
}