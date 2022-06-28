package com.training.music.generics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.training.music.CustomViewHolder

class GenericAdapter<T:Any>(private val dataList: MutableList<T>,
                            @LayoutRes val layoutID: Int,
                            private val bindingInterface: GenericBinding<T>
    ) : RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater
                             .from(parent.context)
                             .inflate(layoutID, parent, false)
        return CustomViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item, bindingInterface)
    }

    override fun getItemCount(): Int  = dataList.size
}