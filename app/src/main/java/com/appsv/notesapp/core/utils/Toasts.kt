package com.appsv.notesapp.core.utils

import android.content.Context
import android.widget.Toast

object Toasts {

    fun showSimpleToast(message : String, context : Context){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

}