package com.project.vllo

import android.media.*
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.project.vllo.databinding.ActivityEditBinding
import com.project.vllo.model.Item
import com.project.vllo.utils.TrimVideoUtils


class EditActivity : AppCompatActivity() {

    val TAG = "EditActivity"

    private lateinit var binding: ActivityEditBinding

    private lateinit var itemList: ArrayList<Item>
    private var mDuration : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        // 없으면 안댐 addFragment 에서 넘어올때 null 체크 필수
        itemList = intent.getParcelableArrayListExtra<Item>("selectedList")!!

        setUpDataBinding()
        initView()
        prepareVideo()

        setVideoDuration()
    }

    private fun setUpDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)
        binding.edit = this@EditActivity
    }

    private fun initView() {
        binding.seekBar.apply {
            progress = binding.seekBar.max/2
            isEnabled = false
        }
    }

    private fun prepareVideo() {
        val player = SimpleExoPlayer.Builder(this).build()
        val mediaItem = MediaItem.fromUri(itemList[0].uri)
        player.apply {
            setMediaItem(mediaItem)
            prepare()
        }
        binding.videoLoader.apply {
            hideController()
            useController = false
            setPlayer(player)
        }
        binding.tvVideoStartTime.text =
            String.format("%s", TrimVideoUtils.stringForTime(player.currentPosition.toFloat()))
        mDuration = itemList[0].duration!!
    }

    private fun setVideoDuration() {
        binding.tvVideoEndTime.text = String.format("%s",TrimVideoUtils.stringForTime(mDuration.toFloat()))
    }

    fun playVideo() {
        binding.btnVideoPause.visibility = View.VISIBLE
        binding.btnVideoPlay.visibility = View.INVISIBLE
        binding.videoLoader.player?.play()
    }
    fun pauseVideo() {
        binding.btnVideoPause.visibility = View.INVISIBLE
        binding.btnVideoPlay.visibility = View.VISIBLE
        binding.videoLoader.player?.pause()
    }

     fun previousVideo(){
         binding.videoLoader.player?.seekTo(0)
         pauseVideo()
     }
     fun nextVideo(){
         binding.videoLoader.player?.seekTo(itemList[0].duration!!.toLong())
         pauseVideo()
     }
}