package com.training.music

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.training.music.generics.GenericBinding


class CustomViewHolder(private val view:View) : RecyclerView.ViewHolder(view) {
    fun <T:Any> bind(
        item:T,
        bindingInterface: GenericBinding<T>
    ) = bindingInterface.bindData(item, view)
}