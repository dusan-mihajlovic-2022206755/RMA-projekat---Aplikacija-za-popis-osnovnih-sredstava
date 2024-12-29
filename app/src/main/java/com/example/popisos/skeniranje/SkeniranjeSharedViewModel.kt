package com.example.popisos.skeniranje

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SkeniranjeSharedViewModel : ViewModel() {
    val popisID = MutableLiveData<Int>()
}