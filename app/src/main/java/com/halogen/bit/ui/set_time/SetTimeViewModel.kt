package com.halogen.bit.ui.set_time

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SetTimeViewModel : ViewModel() {

    val hours = MutableLiveData(0)
    val mins = MutableLiveData(0)
    val secs = MutableLiveData(0)

}