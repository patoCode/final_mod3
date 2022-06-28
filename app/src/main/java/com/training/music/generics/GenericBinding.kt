package com.training.music.generics

import android.view.View

interface GenericBinding <T> {
    fun bindData(item:T, view:View)
}