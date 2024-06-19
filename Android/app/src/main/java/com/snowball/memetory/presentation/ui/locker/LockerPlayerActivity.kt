package com.snowball.memetory.presentation.ui.locker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.snowball.memetory.R

class LockerPlayerActivity : AppCompatActivity() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_locker_player)

        val videoUrl = intent.getStringExtra("videoUrl")

        playerView = findViewById(R.id.playerView)
        initializePlayer(videoUrl!!)
    }

    private fun initializePlayer(videoUrl: String) {
        player = ExoPlayer.Builder(this).build()
        playerView.player = player
        val mediaItem = MediaItem.fromUri(videoUrl)
        player.addMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }
}