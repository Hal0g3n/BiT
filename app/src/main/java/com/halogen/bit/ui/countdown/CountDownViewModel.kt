package com.halogen.bit.ui.countdown

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.halogen.bit.model.Duration
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CountDownViewModel: ViewModel() {

    var hasFailed: Boolean = false
    val duration = MutableLiveData<Duration>()

    /**
     * Starts the timer, callbacks with how long user has focused
     * @param callback Function1<Int, Unit> Given the time focused
     */
    fun start(callback: (Int) -> Unit) = run {

        val seconds = duration.value!!.hours * 3600 + duration.value!!.mins * 60 + duration.value!!.secs

        GlobalScope.launch {
            //Decrement once a second till ZERO
            for (i in 1..seconds) {
                delay(1000)
                duration.postValue(duration.value?.dec())

                //If failed midway => cancel
                if (hasFailed) {
                    //Calc time focused
                    callback.invoke(i)

                    hasFailed = false
                    return@launch
                }
            }

            //Called when time is up
            delay(1000)

            callback.invoke(seconds)
        }

    }

}