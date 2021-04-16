package com.halogen.bit

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import com.firebase.ui.auth.AuthUI

class MainActivity : AppCompatActivity() {

    companion object {
        var onActivityExit: (() -> Unit)? = null
    }

    private val player : MediaPlayer by lazy {MediaPlayer.create(this, R.raw.music)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set up the music
        player.isLooping = true
        player.setVolume(.2f,.2f)
        player.start()
    }

    override fun onPause() {
        super.onPause()

        //Invoke if activity exited
        onActivityExit?.invoke()
    }

}