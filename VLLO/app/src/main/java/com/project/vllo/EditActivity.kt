package com.project.vllo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import android.widget.VideoView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.project.vllo.model.Item

class EditActivity : AppCompatActivity() {

    val TAG = "EditActivity"

    private lateinit var playerView: PlayerView

    private lateinit var itemList: ArrayList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        // 없으면 안댐 addFragment 에서 넘어올때 null 체크 필수
        itemList = intent.getParcelableArrayListExtra<Item>("selectedList")!!

        setFindViewById()

        val player = SimpleExoPlayer.Builder(this).build()
        playerView.player = player

        // val mediaItem = MediaItem.fromUri(itemList[0].uri)
        // player.setMediaItem(mediaItem)
        // player.prepare()

        for(i in itemList){
            player.addMediaItem(MediaItem.fromUri(i.uri))
        }
        player.prepare()
    }

    private fun setFindViewById() {
        playerView = findViewById(R.id.playerView)
    }
}