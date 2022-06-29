package com.training.music

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.training.music.databinding.ActivityArtistBinding
import com.training.music.dto.ArtistDto
import com.training.music.generics.GenericAdapter
import com.training.music.generics.GenericBinding
import com.training.music.network.ApiObject
import com.training.music.response.Artist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ArtistActivity : AppCompatActivity() {

    private lateinit var binding : ActivityArtistBinding
    private lateinit var adapter : GenericAdapter<ArtistDto>
    private val _artistList = mutableListOf<ArtistDto>()
    private val bindingInterface = object : GenericBinding<ArtistDto> {
        override fun bindData(item:ArtistDto, view: View){
            var _tvName = view.findViewById<TextView>(R.id.tvName)
            var _tvAlias = view.findViewById<TextView>(R.id.tvAlias)
            var _ivPic = view.findViewById<ImageView>(R.id.ivPic)
            var _actionEdit = view.findViewById<ImageView>(R.id.actionEdit)
            var _actionRemove = view.findViewById<ImageView>(R.id.actionRemove)

            _tvName!!.text = item.name.toString()
            _tvAlias!!.text = item.alias.toString()
            _ivPic.setBackgroundResource(R.drawable.c01)

            _actionEdit.setOnClickListener{
                editElement(item)
            }
            _actionRemove.setOnClickListener {
                deleteElement(item)
            }
        }
    }

    private fun editElement(_obj: ArtistDto){
        var intent = Intent(this@ArtistActivity, CRUDArtistActivity::class.java)
        intent.putExtra("artist", _obj)
        this.startActivity(intent)
        true
    }

    private fun deleteElement(artist: ArtistDto){
        var dialog = AlertDialog.Builder(this)
        dialog.setTitle("CONFIRMAR")
        dialog.setIcon(R.drawable.ic_delete)
        dialog.setMessage("Esta seguro de eliminar a ${artist.name.uppercase()}?")
        dialog.setPositiveButton("Si", object : DialogInterface.OnClickListener{
            override fun onClick(_dialog: DialogInterface?, p1: Int) {
                if(artist != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val response: Response<*> = ApiObject.getRetro().deleteArtist(artist!!._id)
                        runOnUiThread {
                            if (response.isSuccessful) {
                                list()
                                _dialog!!.dismiss()
                            }else{
                                Toast.makeText(this@ArtistActivity, "Error de eliminacion, verifique e intente nuevamente",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        })
        dialog.setNegativeButton("Cancelar", null)
        dialog.show()
        true
    }


    private fun removeElement(_obj: ArtistDto){
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistBinding.inflate(layoutInflater)
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
        var intent = Intent(this@ArtistActivity, CRUDArtistActivity::class.java)
        startActivity(intent)
    }

    private fun initRecycler(){
        adapter = GenericAdapter<ArtistDto>(_artistList, R.layout.artist_item, bindingInterface)
        binding.rvArtista.layoutManager = GridLayoutManager(this, 2)
        binding.rvArtista.adapter = adapter
    }

    private fun list(){
        CoroutineScope(Dispatchers.IO).launch {
            val _res: Response<Artist>
            _res = ApiObject.getRetro().listArtist()
            val _response = _res.body()!!
            val _list = _response.data
            runOnUiThread{
                if(_res.isSuccessful){
                    _artistList.clear()
                    _artistList.addAll(_list)
                    adapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@ArtistActivity, "ERROR DE CONEXION", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}